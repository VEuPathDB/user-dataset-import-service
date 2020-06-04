package org.veupathdb.service.userds.service;

import java.sql.Date;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.devskiller.friendly_id.FriendlyId;
import com.fasterxml.jackson.databind.JsonNode;
import org.veupathdb.service.userds.Main;
import org.veupathdb.service.userds.generated.model.*;
import org.veupathdb.service.userds.model.JobRow;
import org.veupathdb.service.userds.model.JobStatus;
import org.veupathdb.service.userds.model.MetaValidationResult;
import org.veupathdb.service.userds.model.ProjectCache;
import org.veupathdb.service.userds.repo.InsertJobQuery;
import org.veupathdb.service.userds.repo.SelectJobQuery;
import org.veupathdb.service.userds.repo.SelectJobsQuery;

import static java.util.Collections.singletonList;
import static org.veupathdb.service.userds.generated.model.PrepRequest.*;

public class JobService
{
  /**
   * Look up existing jobs owned by the given user id.
   *
   * @param userId User id to lookup.
   *
   * @return A list of jobs owned by the given user id.  If no jobs were found,
   * the returned list will be empty.
   */
  public static List < JobRow > getJobsByUser(long userId) throws Exception {
    return SelectJobsQuery.run(userId);
  }

  /**
   * Look up an existing job by the given job token/id string.
   *
   * @param jobId Job id to lookup.
   *
   * @return An option which will contain a job instance if a matching job was
   * found.
   */
  public static Optional < JobRow > getJobByToken(String jobId)
  throws Exception {
    return SelectJobQuery.run(jobId);
  }

  public static String insertJob(PrepRequest req, long userId)
  throws Exception {
    final var jobId = FriendlyId.createFriendlyId();
    InsertJobQuery.run(prepToJob(req, jobId, userId));
    return jobId;
  }

  private static final String
    valErrBlankName = "Dataset name cannot be blank.";

  /**
   * Perform input data validation for a job creation request body.
   *
   * @param req Job creation request body
   *
   * @return An option which will contain a validation error set if validation
   * fails.
   */
  public static Optional < MetaValidationResult > validateJobMeta(
    final PrepRequest req
  ) {
    var out = new MetaValidationResult();

    // Verify the request has a non-empty name
    if (req.getDatasetName() == null || req.getDatasetName().isBlank()) {
      out.getByKey().put(KEY_DS_NAME, singletonList(valErrBlankName));
    }

    // Verify that there are handlers configured for the selected jobs
    var projects = validateProjectsKey(req.getProjects());
    if (!projects.isEmpty()) {
      out.getByKey().put(KEY_PROJECTS, projects);
    }

    var type = validateTypeKey(req.getDatasetType(), req);
    if (!type.isEmpty()) {
      out.getByKey().put(KEY_DS_TYPE, type);
    }

    return out.containsErrors() ? Optional.of(out) : Optional.empty();
  }

  /**
   * Convert a JobRow instance to an output StatusResponse instance.
   *
   * @param row Row to convert.
   *
   * @return StatusResponse object.
   */
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
          .toInstant()))
      .setFinished(row.getFinished()
        .map(d -> d.atZone(ZoneId.systemDefault()))
        .map(ZonedDateTime::toInstant)
        .map(Date::from)
        .orElse(null));

    // If the job status was "errored" then we only have an exception message
    // to return.
    if (row.getStatus() == JobStatus.ERRORED) {
      out.setStatusDetails(new StatusResponseImpl.StatusDetailsTypeImpl(
        new JobErrorImpl()
          .setMessage(row.getMessage().map(JsonNode::textValue).orElse(null))));

    // If the job status was "rejected" then we have validation errors to
    // return.
    } else if (row.getStatus() == JobStatus.REJECTED && row.getMessage().isPresent()) {
      var dets = new ValidationErrorsImpl();
      var raw  = row.getMessage().get();

      if (raw.has("general")) {
        raw.get("general")
          .forEach(j -> dets.getErrors().getGeneral().add(j.textValue()));
      }

      if (raw.has("byKey")) {
        var obj = raw.get("byKey");
        obj.fieldNames()
          .forEachRemaining(k -> dets.getErrors()
            .getByKey()
            .setAdditionalProperties(k, obj.get(k)));
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

  /**
   * Error messages for input "projects" key validation.
   */
  private static final String
    valErrBadProject = "Unrecognized project %s",
    valErrNoProjects = "At least one target project must be provided.",
    valErrNoProHands = "No handlers configured for project %s";

  /**
   * Input "projects" key validation
   */
  private static List < String > validateProjectsKey(List < String > projects) {
    final var out = new ArrayList < String >();

    // Verify the request has a non-empty project array
    if (projects == null || projects.isEmpty()) {
      out.add(valErrNoProjects);
    } else {
      projects.stream()
        // Filter out and warn about bad projects
        .filter(p -> {
          if (!ProjectCache.getInstance().containsKey(p)) {
            out.add(String.format(valErrBadProject, p));
            return false;
          }
          return true;
        })

        // Warn about projects with no handlers
        .forEach(p -> {
          if (!Main.jsonConfig.getProjects().contains(p)) {
            out.add(String.format(valErrNoProHands, p));
          }
        });
    }

    return out;
  }

  /**
   * Error messages for input "datasetType" key validation.
   */
  private static final String
    valErrBlankType   = "Dataset type cannot be blank.",
    valErrNoHandlers  = "No handlers configured for dataset type %s.",
    valErrUnsupported = "Project %s has no handlers for dataset type %s.";

  /**
   * Input "datasetType" key validation.
   */
  private static List < String > validateTypeKey(String type, PrepRequest req) {
    final var out = new ArrayList < String >();

    // Verify the request has a non-empty dataset type that matches with the
    // configured services.
    if (type == null || type.isBlank()) {
      out.add(valErrBlankType);
    } else if (!Main.jsonConfig.getByType().containsKey(type)) {
      out.add(String.format(valErrNoHandlers, type));
    } else {
      var set = Main.jsonConfig.getByType().get(type);

      for (var p : req.getProjects()) {
        if (!set.contains(p)) {
          out.add(String.format(valErrUnsupported, p, type));
        }
      }
    }

    return out;
  }
}
