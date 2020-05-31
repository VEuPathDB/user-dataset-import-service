package org.veupathdb.service.userds.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


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
  private final String          message;
  private final String          fileName;
  private final Long            fileSize;
  private final List < String > projects;

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
    String message,
    String fileName,
    Long fileSize,
    List < String > projects
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
    this.fileName = fileName;
    this.fileSize = fileSize;
    this.projects = projects;
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

  public Optional < String > getMessage() {
    return Optional.ofNullable(message);
  }

  public Optional < String > getFileName() {
    return Optional.ofNullable(fileName);
  }

  public Optional < Long > getFileSize() {
    return Optional.ofNullable(fileSize);
  }

  public List < String > getProjects() {
    return projects;
  }
}
