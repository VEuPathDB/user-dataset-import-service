package org.veupathdb.service.userds.service;

import java.sql.Date;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import org.veupathdb.service.userds.Main;
import org.veupathdb.service.userds.generated.model.*;
import org.veupathdb.service.userds.model.JobRow;
import org.veupathdb.service.userds.model.JobStatus;
import org.veupathdb.service.userds.repo.SelectJobQuery;
import org.veupathdb.service.userds.repo.SelectJobsQuery;

public class JobService
{
  public static List < JobRow > getJobsByUser(long userId) throws Exception {
    return SelectJobsQuery.run(userId);
  }

  public static Optional < JobRow > getJobByToken(String jobId)
  throws Exception {
    return SelectJobQuery.run(jobId);
  }

  private static final String
    valErrBlankName  = "Dataset name cannot be blank.",
    valErrNoProjects = "At least one target project must be provided.",
    valErrBlankType  = "Dataset type cannot be blank.",
    valErrUnsupported = "Project %s has no handlers for dataset type %s.";

  public static Optional < InvalidInputError.ErrorsType > validateJobMeta(
    final PrepRequest req
  ) {
    var respond = false;
    var out     = new InvalidInputErrorImpl.ErrorsTypeImpl();

    // Verify the request has a non-empty name
    if (req.getDatasetName() == null || req.getDatasetName().isBlank()) {
      respond = true;
      out.getByKey().setAdditionalProperties(
        PrepRequest.KEY_DS_NAME, new String[] {valErrBlankName});
    }

    // Verify the request has a non-empty project array
    if (req.getProjects() == null || req.getProjects().isEmpty()) {
      respond = true;
      out.getByKey().setAdditionalProperties(PrepRequest.KEY_PROJECTS,
        new String[]{valErrNoProjects});
    }

    // Verify the request has a non-empty dataset type that matches with the
    // configured services.
    if (req.getDatasetType() == null || req.getDatasetType().isBlank()) {
      respond = true;
      out.getByKey()
        .setAdditionalProperties(PrepRequest.KEY_DS_TYPE,
          new String[]{valErrBlankType});
    } else {
      // Get relevant service entries for projects
      var matches = Arrays.stream(Main.jsonConfig.getServices())
        .filter(svc -> Arrays.stream(svc.getProjects())
          .anyMatch(req.getProjects()::contains))
        .collect(Collectors.toList());

      // For each matching service, verify that the dataset type is available
      // for that service.
      for (var n : matches) {
        if (!n.getDsType().equals(req.getDatasetType())) {
          respond = true;
          Arrays.stream(n.getProjects())
            .filter(req.getProjects()::contains)
            .map(p -> String.format(valErrUnsupported, p, req.getDatasetType()))
            .forEach(out.getGeneral()::add);
        }
      }
    }

    return respond ? Optional.of(out) : Optional.empty();
  }

  public static StatusResponse rowToStatus(JobRow row) {
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
      out.setStatusDetails(new StatusResponseImpl.StatusDetailsTypeImpl(
        new JobErrorImpl()
          .setMessage(row.getMessage().map(JsonNode::textValue).orElse(null))));
    } else if (row.getStatus() == JobStatus.REJECTED && row.getMessage().isPresent()) {
      var dets = new ValidationErrorsImpl();
      var raw  = row.getMessage().get();

      if (raw.has("general")) {
        raw.get("general")
          .forEach(j -> dets.getErrors().getGeneral().add(raw.textValue()));
      }

      if (raw.has("byKey")) {
        var obj = raw.get("byKey");
        obj.fieldNames()
          .forEachRemaining(k -> dets.getErrors()
            .getByKey()
            .setAdditionalProperties(k, obj.get(k).textValue()));
      }

      out.setStatusDetails(new StatusResponseImpl.StatusDetailsTypeImpl(dets));
    }

    return out;
  }

  public static JobRow prepToJob(PrepRequest body, String jobId, long userId) {
    return new JobRow(0, jobId, userId, JobStatus.AWAITING_UPLOAD,
      body.getDatasetName(), body.getDescription(), body.getSummary(), null,
      null, null, body.getProjects()
    );
  }
}
