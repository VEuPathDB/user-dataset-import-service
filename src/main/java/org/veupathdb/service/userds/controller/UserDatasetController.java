package org.veupathdb.service.userds.controller;

import java.io.InputStream;
import java.util.stream.Collectors;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;

import com.devskiller.friendly_id.FriendlyId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.veupathdb.lib.container.jaxrs.providers.UserProvider;
import org.veupathdb.lib.container.jaxrs.server.annotations.Authenticated;
import org.veupathdb.service.userds.generated.model.InvalidInputErrorImpl;
import org.veupathdb.service.userds.generated.model.PrepRequest;
import org.veupathdb.service.userds.generated.model.PrepResponseImpl;
import org.veupathdb.service.userds.generated.model.ProcessResponseImpl;
import org.veupathdb.service.userds.generated.resources.UserDatasets;
import org.veupathdb.service.userds.repo.InsertJobQuery;
import org.veupathdb.service.userds.service.Importer;
import org.veupathdb.service.userds.service.JobService;
import org.veupathdb.service.userds.service.ThreadProvider;
import org.veupathdb.service.userds.util.ErrFac;
import org.veupathdb.service.userds.util.Errors;
import org.veupathdb.service.userds.util.InputStreamNotifier;
import org.veupathdb.service.userds.util.http.Header;

@Authenticated
public class UserDatasetController implements UserDatasets
{
  static final String
    errRowFetch      = "failed to fetch user's job rows",
    errJobCreate     = "failed to create new job entry",
    errProcessImport = "error when processing import",
    errContentType   = "missing or invalid Content-Type header";

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
  public GetResponse getUserJobs() {
    try {
      return GetResponse.respond200(
        JobService.getJobsByUser(UserProvider.lookupUser(req)
          .orElseThrow()
          .getUserId())
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
      return JobService.getJobByToken(jobId)
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
    var val = JobService.validateJobMeta(entity);

    if (val.isPresent()) {
      var out = new InvalidInputErrorImpl();
      out.setErrors(val.get());
      return PostResponse.respond422(out);
    }

    try {
      var user  = UserProvider.lookupUser(req).orElseThrow();
      var jobId = FriendlyId.createFriendlyId();
      var job   = JobService.prepToJob(entity, jobId, user.getUserId());

      InsertJobQuery.run(job);

      return PostResponse.respond200(new PrepResponseImpl().setJobId(jobId));
    } catch (Throwable e) {
      log.error(errJobCreate, e);
      return PostResponse.respond500(ErrFac.new500(req, e));
    }
  }

  @Override
  public PostByJobIdResponse postImport(String jobId, InputStream body) {
    try {
      var job = JobService.getJobByToken(jobId)
        .orElseThrow(NotFoundException::new);
      var lock = new Object();
      var bound = Header.getBoundaryString(headers)
        .orElseThrow(() -> new BadRequestException(errContentType));

      //noinspection SynchronizationOnLocalVariableOrMethodParameter
      synchronized (lock) {
        var pipeWrap = new InputStreamNotifier(body, lock);
        ThreadProvider.newThread(new Importer(job, bound, pipeWrap)).start();
        lock.wait();
      }

      return PostByJobIdResponse.respond200(new ProcessResponseImpl());
    } catch (Throwable e) {
      log.error(errProcessImport, e);
      return PostByJobIdResponse.respond500(ErrFac.new500(req, e));
    } finally {
      Errors.swallow(body::close);
    }
  }
}
