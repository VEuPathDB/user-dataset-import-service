package org.veupathdb.service.userds.generated.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "dataset",
    "description",
    "summary",
    "stepPercent",
    "projects",
    "status",
    "message",
    "started"
})
public class StatusResponseImpl implements StatusResponse {
  @JsonProperty("id")
  private String id;

  @JsonProperty("dataset")
  private String dataset;

  @JsonProperty("description")
  private String description;

  @JsonProperty("summary")
  private String summary;

  @JsonProperty("stepPercent")
  private int stepPercent;

  @JsonProperty("projects")
  private List<ProjectType> projects;

  @JsonProperty("status")
  private JobStatus status;

  @JsonProperty("message")
  private String message;

  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
  )
  @JsonDeserialize(
      using = TimestampDeserializer.class
  )
  @JsonProperty("started")
  private Date started;

  @JsonProperty("id")
  public String getId() {
    return this.id;
  }

  @JsonProperty("id")
  public void setId(String id) {
    this.id = id;
  }

  @JsonProperty("dataset")
  public String getDataset() {
    return this.dataset;
  }

  @JsonProperty("dataset")
  public void setDataset(String dataset) {
    this.dataset = dataset;
  }

  @JsonProperty("description")
  public String getDescription() {
    return this.description;
  }

  @JsonProperty("description")
  public void setDescription(String description) {
    this.description = description;
  }

  @JsonProperty("summary")
  public String getSummary() {
    return this.summary;
  }

  @JsonProperty("summary")
  public void setSummary(String summary) {
    this.summary = summary;
  }

  @JsonProperty("stepPercent")
  public int getStepPercent() {
    return this.stepPercent;
  }

  @JsonProperty("stepPercent")
  public void setStepPercent(int stepPercent) {
    this.stepPercent = stepPercent;
  }

  @JsonProperty("projects")
  public List<ProjectType> getProjects() {
    return this.projects;
  }

  @JsonProperty("projects")
  public void setProjects(List<ProjectType> projects) {
    this.projects = projects;
  }

  @JsonProperty("status")
  public JobStatus getStatus() {
    return this.status;
  }

  @JsonProperty("status")
  public void setStatus(JobStatus status) {
    this.status = status;
  }

  @JsonProperty("message")
  public String getMessage() {
    return this.message;
  }

  @JsonProperty("message")
  public void setMessage(String message) {
    this.message = message;
  }

  @JsonProperty("started")
  public Date getStarted() {
    return this.started;
  }

  @JsonProperty("started")
  public void setStarted(Date started) {
    this.started = started;
  }
}
