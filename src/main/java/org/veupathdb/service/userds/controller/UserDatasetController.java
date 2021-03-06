package org.veupathdb.service.userds.controller;

import java.io.InputStream;
import java.util.stream.Collectors;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gusdb.fgputil.accountdb.UserProfile;
import org.veupathdb.lib.container.jaxrs.errors.UnprocessableEntityException;
import org.veupathdb.lib.container.jaxrs.providers.UserProvider;
import org.veupathdb.lib.container.jaxrs.server.annotations.Authenticated;
import org.veupathdb.service.userds.generated.model.PrepRequest;
import org.veupathdb.service.userds.generated.model.PrepResponseImpl;
import org.veupathdb.service.userds.generated.model.ProcessResponseImpl;
import org.veupathdb.service.userds.generated.resources.UserDatasets;
import org.veupathdb.service.userds.model.JobStatus;
import org.veupathdb.service.userds.model.handler.DatasetOrigin;
import org.veupathdb.service.userds.repo.SelectJobsQuery;
import org.veupathdb.service.userds.service.Importer;
import org.veupathdb.service.userds.service.JobService;
import org.veupathdb.service.userds.service.ThreadProvider;
import org.veupathdb.service.userds.util.ErrFac;
import org.veupathdb.service.userds.util.Errors;
import org.veupathdb.service.userds.util.InputStreamNotifier;
import org.veupathdb.service.userds.util.http.Header;

import static org.veupathdb.service.userds.service.JobService.*;

@Authenticated
public class UserDatasetController implements UserDatasets
{
  static final String
    errRowFetch      = "failed to fetch user's job rows",
    errJobCreate     = "failed to create new job entry",
    errProcessImport = "error when processing import",
    errContentType   = "missing or invalid Content-Type header",
    errDoubleStart   = "cannot resubmit an upload to a started job",
    errDelJobRunning = "cannot delete a job that is in progress";

  private final Logger log;

  private final Request req;

  private final HttpHeaders headers;

  public UserDatasetController(
    final @Context Request req,
    final @Context HttpHeaders headers
  ) {
    this.req = req;
    this.headers = headers;
    this.log = LogManager.getLogger(getClass());
  }

  @Override
  public GetResponse getUserJobs(Integer limit, Integer page) {
    if (limit != null) {
      if (limit < 0) {
        throw new BadRequestException("limit must not be less than 0");
      }
    } else {
      limit = SelectJobsQuery.DEFAULT_LIMIT;
    }

    if (page != null) {
      if (page < 0) {
        throw new BadRequestException("page must not be less than 0");
      }
    } else {
      page = SelectJobsQuery.DEFAULT_OFFSET;
    }

    try {
      return GetResponse.respond200(getJobsByUser(UserProvider.lookupUser(req)
        .map(UserProfile::getUserId)
        .orElseThrow(), limit, page)
        .stream()
        .map(JobService::rowToStatus)
        .collect(Collectors.toList()));
    } catch (Exception e) {
      log.error(errRowFetch, e);
      return GetResponse.respond500(ErrFac.new500(req, e));
    }
  }

  @Override
  public GetByJobIdResponse getJob(String jobId) {
    try {
      return getJobByToken(jobId)
        .map(JobService::rowToStatus)
        .map(GetByJobIdResponse::respond200)
        .orElseGet(() -> GetByJobIdResponse.respond404(ErrFac.new404()));
    } catch (Exception e) {
      log.error(errRowFetch, e);
      return GetByJobIdResponse.respond500(ErrFac.new500(req, e));
    }
  }

  @Override
  public PostResponse createJob(PrepRequest entity) {
    validateJobMeta(entity).ifPresent(r -> {
      throw new UnprocessableEntityException(r.getGeneral(), r.getByKey());
    });

    // TODO: This field will become required when Galaxy runs imports through
    //       this service.
    if (entity.getOrigin() == null)
      entity.setOrigin(DatasetOrigin.DIRECT_UPLOAD);

    try {
      return PostResponse.respond200(new PrepResponseImpl()
        .setJobId(JobService.insertJob(entity, UserProvider.lookupUser(req)
          .map(UserProfile::getUserId)
          .orElseThrow())));
    } catch (Throwable e) {
      log.error(errJobCreate, e);
      return PostResponse.respond500(ErrFac.new500(req, e));
    }
  }

  @Override
  public void deleteJob(String jobId) {
    try {
      var job = getJobByToken(jobId).orElseThrow(NotFoundException::new);

      switch (job.getStatus()) {
        case AWAITING_UPLOAD, REJECTED, ERRORED, SUCCESS -> deleteJobById(job.getDbId());
        default -> throw new BadRequestException(errDelJobRunning);
      }
    } catch (WebApplicationException e) {
      // Don't catch Jax-RS exceptions.
      throw e;
    } catch (Exception e) {
      log.error(errRowFetch, e);
      throw new InternalServerErrorException(e);
    }
  }

  @Override
  public PostByJobIdResponse postImport(String jobId, InputStream body) {
    try {
      var job = getJobByToken(jobId)
        .orElseThrow(NotFoundException::new);

      if (job.getStatus() != JobStatus.AWAITING_UPLOAD)
        throw new BadRequestException(errDoubleStart);

      var lock = new Object();
      var bound = Header.getBoundaryString(headers)
        .orElseThrow(() -> new BadRequestException(errContentType));

      //noinspection SynchronizationOnLocalVariableOrMethodParameter
      synchronized (lock) {
        ThreadProvider.newThread(new Importer(
          job,
          bound,
          new InputStreamNotifier(body, lock)
        )).start();
        lock.wait();
      }

      return PostByJobIdResponse.respond200(new ProcessResponseImpl());
    } catch (WebApplicationException e) {
      // Don't catch Jax-RS exceptions.
      throw e;
    } catch (Throwable e) {
      log.error(errProcessImport, e);
      return PostByJobIdResponse.respond500(ErrFac.new500(req, e));
    } finally {
      Errors.swallow(body::close);
    }
  }
}
