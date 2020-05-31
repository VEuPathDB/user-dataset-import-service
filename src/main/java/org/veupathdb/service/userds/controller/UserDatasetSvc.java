package org.veupathdb.service.userds.controller;

import java.io.InputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.veupathdb.lib.container.jaxrs.middleware.AuthFilter;
import org.veupathdb.lib.container.jaxrs.providers.UserProvider;
import org.veupathdb.service.userds.generated.model.*;
import org.veupathdb.service.userds.generated.resources.UserDatasets;
import org.veupathdb.service.userds.model.JobRow;
import org.veupathdb.service.userds.repo.SelectJobQuery;
import org.veupathdb.service.userds.repo.SelectJobsQuery;
import org.veupathdb.service.userds.service.DatasetProcessor;
import org.veupathdb.service.userds.util.ErrFac;
import org.veupathdb.service.userds.util.Errors;
import org.veupathdb.service.userds.util.InputStreamNotifier;

@AuthFilter.Authenticated
public class UserDatasetSvc implements UserDatasets
{
  static final String
    errRowFetch    = "failed to fetch user's job rows";


  private final Logger      log;
  private final Request     req;

  public UserDatasetSvc(@Context Request req) {
    this.req = req;
    this.log = LogManager.getLogger(getClass());
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
    final String jobId,
    final InputStream body
  ) {
    try {
      var optJob = SelectJobQuery.run(jobId);

      if (optJob.isEmpty())
        return PostUserDatasetsByJobIdResponse
          .respond404WithApplicationJson(ErrFac.new404());

      var pipeWrap = new InputStreamNotifier(body, this);

      new Thread(() -> new DatasetProcessor(optJob.get(), pipeWrap)
        .process()
      ).start();

      wait();

    } catch (Throwable e) {
      log.error(e);
      return PostUserDatasetsByJobIdResponse
        .respond500WithApplicationJson(ErrFac.new500(req, e));
    } finally {
      Errors.swallow(body::close);
    }

    return PostUserDatasetsByJobIdResponse
      .respond200WithApplicationJson(new ProcessResponseImpl());
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
    return out;
  }
}
