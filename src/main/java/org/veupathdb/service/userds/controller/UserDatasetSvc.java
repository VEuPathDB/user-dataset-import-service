package org.veupathdb.service.userds.controller;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.veupathdb.lib.container.jaxrs.middleware.AuthFilter;
import org.veupathdb.lib.container.jaxrs.providers.UserProvider;
import org.veupathdb.service.userds.generated.model.*;
import org.veupathdb.service.userds.generated.resources.UserDatasets;
import org.veupathdb.service.userds.model.JobRow;
import org.veupathdb.service.userds.model.JobStatus;
import org.veupathdb.service.userds.repo.InsertFileQuery;
import org.veupathdb.service.userds.repo.SelectJobQuery;
import org.veupathdb.service.userds.repo.SelectJobsQuery;
import org.veupathdb.service.userds.util.ErrFac;

@AuthFilter.Authenticated
public class UserDatasetSvc implements UserDatasets
{
  static final String
    errRowFetch    = "failed to fetch user's job rows",
    errUploadFile  = "job state is receiving-from-client but no file name is set",
    errUploadSize  = "job state is receiving-from-client but no file size is set",
    errTmpFileGone = "job state is receiving-from-client but tmp file is missing";


  private final Logger      log;
  private final Request     req;
  private final HttpHeaders headers;

  public UserDatasetSvc(@Context Request req, @Context HttpHeaders headers) {
    this.req = req;
    this.log = LogManager.getLogger(getClass());
    this.headers = headers;
  }

  @Override
  public GetUserDatasetsResponse getUserDatasets() {
    final List < JobRow > rows;

    // orElseThrow as this should not be possible
    var user = UserProvider.lookupUser(req).orElseThrow();

    try {
      rows = SelectJobsQuery.run(user.getUserId());
    } catch (SQLException e) {
      log.error(errRowFetch, e);
      throw new InternalServerErrorException(errRowFetch);
    }

    return GetUserDatasetsResponse.respond200WithApplicationJson(rows.stream()
      .map(UDSvcUtil::rowToStatus)
      .collect(Collectors.toList()));
  }

  @Override
  public PostUserDatasetsResponse postUserDatasets(PrepRequest entity) {
    return null;
  }

  @Override
  public GetUserDatasetsByJobIdResponse getUserDatasetsByJobId(String jobId) {
    try {
      var optJob = SelectJobQuery.run(jobId);

      if (optJob.isEmpty())
        return GetUserDatasetsByJobIdResponse.respond404WithApplicationJson(
          ErrFac.new404());

      return GetUserDatasetsByJobIdResponse.respond200WithApplicationJson(
        UDSvcUtil.rowToStatus(optJob.get()));

    } catch (SQLException e) {
      log.error(errRowFetch, e);
      throw new InternalServerErrorException(errRowFetch);
    }
  }

  @Override
  public PostUserDatasetsByJobIdResponse postUserDatasetsByJobId(
    String jobId,
    InputStream body,
    FormDataContentDisposition fileInfo
  ) {
    try {
      final Optional < JobRow > optJob;
      final Workspace           wk;

      optJob = SelectJobQuery.run(jobId);

      if (optJob.isEmpty())
        return PostUserDatasetsByJobIdResponse
          .respond404WithApplicationJson(ErrFac.new404());

      var job      = optJob.get();
      var fileName = fileInfo.getFileName();
      var fileSize = fileInfo.getSize();

      InsertFileQuery.run(job.getDbId(), fileName, fileSize);

      wk = Workspace.create(job.getJobId());
      wk.copyTo(fileName, body);
    } catch (Throwable e) {
      log.error(e);
      return PostUserDatasetsByJobIdResponse
        .respond500WithApplicationJson(ErrFac.new500(req, e));
    } finally {
      try {
        body.close();
      } catch (IOException e) {
        log.error(e);
      }
    }

    var out = new ProcessResponseImpl();
    out.setStatus(ProcessResponse.StatusType.OK);
    return PostUserDatasetsByJobIdResponse.respond200WithApplicationJson(out);
  }
}

class UDSvcUtil
{
  static StatusResponse rowToStatus(JobRow row) {
    var out = new StatusResponseImpl();
    out.setId(row.getJobId());
    out.setDatasetName(row.getName());
    row.getDescription().ifPresent(out::setDescription);
    row.getSummary().ifPresent(out::setSummary);
    out.setStatus(row.getStatus().getName());
    out.setProjects(row.getProjects());
    out.setStarted(Date.from(
      row.getStarted()
        .atZone(ZoneId.systemDefault())
        .toInstant()));

    if (row.getStatus() == JobStatus.RECEIVING_FROM_CLIENT) {
      var fs = row.getFileSize()
        .orElseThrow(() ->
          new IllegalStateException(UserDatasetSvc.errUploadSize));
      var fn = row.getFileName()
        .orElseThrow(() ->
          new IllegalStateException(UserDatasetSvc.errUploadFile));
      var wk = Workspace.open(row.getJobId());
      var cs = wk.sizeOf(fn);
      out.setStepPercent((int) Math.round(((double) fs) / ((double) cs) * 100D));
    }
  }
}
