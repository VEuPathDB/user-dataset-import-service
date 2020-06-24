package org.veupathdb.service.userds.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import org.veupathdb.service.userds.model.handler.DatasetOrigin;


public class JobRow
{
  private final int             dbId;
  private final String          jobId;
  private final long            userId;
  private final JobStatus       status;
  private final String          name;
  private final String          description;
  private final String          summary;
  private final LocalDateTime   started;
  private final LocalDateTime   finished;
  private final JsonNode        message;
  private final List < String > projects;
  private final int             irodsId;
  private final DatasetOrigin   origin;

  public JobRow(
    int dbId,
    String jobId,
    long userId,
    JobStatus status,
    String name,
    String description,
    String summary,
    LocalDateTime started,
    LocalDateTime finished,
    JsonNode message,
    List < String > projects,
    DatasetOrigin origin,
    int irodsId
  ) {
    this.dbId = dbId;
    this.jobId = jobId;
    this.userId = userId;
    this.status = status;
    this.name = name;
    this.description = description;
    this.summary = summary;
    this.started = started;
    this.finished = finished;
    this.message = message;
    this.projects = projects;
    this.origin = origin;
    this.irodsId = irodsId;
  }

  public JobRow(
    String jobId,
    long userId,
    JobStatus status,
    String name,
    String description,
    String summary,
    List < String > projects,
    DatasetOrigin origin
  ) {
    this(0, jobId, userId, status, name, description, summary, null, null,
      null, projects, origin, 0);
  }

  public int getDbId() {
    return dbId;
  }

  public String getJobId() {
    return jobId;
  }

  public long getUserId() {
    return userId;
  }

  public JobStatus getStatus() {
    return status;
  }

  public String getName() {
    return name;
  }

  public Optional < String > getDescription() {
    return Optional.ofNullable(description);
  }

  public Optional < String > getSummary() {
    return Optional.ofNullable(summary);
  }

  public LocalDateTime getStarted() {
    return started;
  }

  public Optional < LocalDateTime > getFinished() {
    return Optional.ofNullable(finished);
  }

  public Optional < JsonNode > getMessage() {
    return Optional.ofNullable(message);
  }

  public List < String > getProjects() {
    return projects;
  }

  public int getIrodsId() {
    return irodsId;
  }

  public DatasetOrigin getOrigin() {
    return origin;
  }
}
