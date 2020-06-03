package org.veupathdb.service.userds.controller;

import java.io.InputStream;
import java.sql.Date;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;

import com.devskiller.friendly_id.FriendlyId;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.veupathdb.lib.container.jaxrs.server.annotations.Authenticated;
import org.veupathdb.lib.container.jaxrs.providers.UserProvider;
import org.veupathdb.service.userds.Main;
import org.veupathdb.service.userds.generated.model.*;
import org.veupathdb.service.userds.generated.resources.UserDatasets;
import org.veupathdb.service.userds.model.JobRow;
import org.veupathdb.service.userds.model.JobStatus;
import org.veupathdb.service.userds.model.Service;
import org.veupathdb.service.userds.repo.InsertJobQuery;
import org.veupathdb.service.userds.repo.SelectJobQuery;
import org.veupathdb.service.userds.repo.SelectJobsQuery;
import org.veupathdb.service.userds.service.Importer;
import org.veupathdb.service.userds.service.ThreadProvider;
import org.veupathdb.service.userds.util.ErrFac;
import org.veupathdb.service.userds.util.Errors;
import org.veupathdb.service.userds.util.InputStreamNotifier;

@Authenticated
public class UserDatasetSvc implements UserDatasets
{
  static final String
    errRowFetch = "failed to fetch user's job rows";


  private final Logger  log;
  private final Request req;
  private final HttpHeaders headers;

  public UserDatasetSvc(@Context Request req, @Context HttpHeaders headers) {
    this.req = req;
    this.headers = headers;
    this.log = LogManager.getLogger(getClass());
  }

  @Override
  public GetResponse getUserDatasets() {
    final List < JobRow > rows;

    // orElseThrow as this should not be possible
    var user = UserProvider.lookupUser(req).orElseThrow();

    try {
      rows = SelectJobsQuery.run(user.getUserId());
    } catch (Exception e) {
      log.error(errRowFetch, e);
      return GetResponse.respond500(ErrFac.new500(req, e));
    }

    return GetResponse.respond200(rows.stream()
      .map(UDSvcUtil::rowToStatus)
      .collect(Collectors.toList()));
  }

  @Override
  public PostResponse postUserDatasets(PrepRequest entity) {
    var val = UDSvcUtil.validate(entity);

    if (val.isPresent()) {
      var out = new InvalidInputErrorImpl();
      out.setErrors(val.get());
      return PostResponse.respond422(out);
    }

    try {
      var user  = UserProvider.lookupUser(req).orElseThrow();
      var jobId = FriendlyId.createFriendlyId();
      var job   = UDSvcUtil.prepToJob(entity, jobId, user.getUserId());

      InsertJobQuery.run(job);

      return PostResponse.respond200(new PrepResponseImpl().setJobId(jobId));
    } catch (Throwable e) {
      log.error("Failed to create new job entry", e);
      return PostResponse.respond500(ErrFac.new500(req, e));
    }
  }

  @Override
  public GetByJobIdResponse getUserDatasetsByJobId(String jobId) {
    try {
      var optJob = SelectJobQuery.run(jobId);

      if (optJob.isEmpty())
        return GetByJobIdResponse.respond404(ErrFac.new404());

      return GetByJobIdResponse.respond200(UDSvcUtil.rowToStatus(optJob.get()));
    } catch (Exception e) {
      log.error(errRowFetch, e);
      return GetByJobIdResponse.respond500(ErrFac.new500(req, e));
    }
  }

  @Override
  public PostByJobIdResponse postUserDatasetsByJobId(
    final String jobId,
    final InputStream body
  ) {
    try {
      var optJob = SelectJobQuery.run(jobId);

      if (optJob.isEmpty())
        return PostByJobIdResponse.respond404(ErrFac.new404());

      var lock = new Object();

      var boundary = headers.getHeaderString("Content-Type")
        .split("boundary=")[1]
        .split(";")[0];

      synchronized (lock) {
        var pipeWrap = new InputStreamNotifier(body, lock);
        ThreadProvider.newThread(new Importer(optJob.get(), boundary, pipeWrap))
          .start();
        lock.wait();
      }

    } catch (Throwable e) {
      log.error("Error when processing import: ", e);
      return PostByJobIdResponse.respond500(ErrFac.new500(req, e));
    } finally {
      Errors.swallow(body::close);
    }

    return PostByJobIdResponse.respond200(new ProcessResponseImpl());
  }
}

class UDSvcUtil
{
  static StatusResponse rowToStatus(JobRow row) {
    var out = new StatusResponseImpl()
      .setId(row.getJobId())
      .setDatasetName(row.getName())
      .setDescription(row.getDescription().orElse(null))
      .setSummary(row.getSummary().orElse(null))
      .setStatus(row.getStatus().getName())
      .setProjects(row.getProjects())
      .setStarted(Date.from(
        row.getStarted()
          .atZone(ZoneId.systemDefault())
          .toInstant()));

    if (row.getStatus() == JobStatus.ERRORED) {
      var dets = new JobErrorImpl();
      dets.setMessage(row.getMessage().map(JsonNode::textValue).orElse(null));
      out.setStatusDetails(new StatusResponseImpl.StatusDetailsTypeImpl(dets));
    } else if (row.getStatus() == JobStatus.REJECTED && row.getMessage().isPresent()) {
      var dets  = new ValidationErrorsImpl();
      var errs  = new ValidationErrorsImpl.ErrorsTypeImpl();
      var byKey = new ValidationErrorsImpl.ErrorsTypeImpl.ByKeyTypeImpl();
      var gener = new ArrayList <String>();
      var raw   = row.getMessage().get();

      if (raw.has("general")) {
        raw.get("general")
          .forEach(j -> gener.add(raw.textValue()));
      }

      if (raw.has("byKey")) {
        var obj = raw.get("byKey");
        obj.fieldNames().forEachRemaining(k ->
          byKey.setAdditionalProperties(k, obj.get(k).textValue()));
      }

      dets.setErrors(errs);
      out.setStatusDetails(new StatusResponseImpl.StatusDetailsTypeImpl(dets));
    }

    return out;
  }

  static JobRow prepToJob(PrepRequest body, String jobId, long userId) {
    return new JobRow(0, jobId, userId, JobStatus.AWAITING_UPLOAD,
      body.getDatasetName(), body.getDescription(), body.getSummary(), null,
      null, null, body.getProjects());
  }

  static Optional < InvalidInputError.ErrorsType > validate(PrepRequest body) {
    var respond = false;
    var out     = new InvalidInputErrorImpl.ErrorsTypeImpl();
    out.setByKey(new InvalidInputErrorImpl.ErrorsTypeImpl.ByKeyTypeImpl());

    if (body.getDatasetName() == null || body.getDatasetName().isBlank()) {
      respond = true;
      out.getByKey().setAdditionalProperties(
        "datasetName",
        "Dataset name cannot be blank."
      );
    }

    if (body.getProjects() == null || body.getProjects().isEmpty()) {
      respond = true;
      out.getByKey().setAdditionalProperties(
        "projects",
        "At least one target project must be provided."
      );
    }

    if (body.getDatasetType() == null || body.getDatasetType().isBlank()) {
      respond = true;
      out.getByKey().setAdditionalProperties(
        "datasetType",
        "Dataset type cannot be blank."
      );
    } else if (
      Arrays.stream(Main.jsonConfig.getServices())
        .map(Service::getDsType)
        .noneMatch(body.getDatasetType()::equals)
    ) {
      respond = true;
      out.getByKey().setAdditionalProperties(
        "datasetType",
        "Unsupported dataset type."
      );
    }

    return respond ? Optional.of(out) : Optional.empty();
  }
}
