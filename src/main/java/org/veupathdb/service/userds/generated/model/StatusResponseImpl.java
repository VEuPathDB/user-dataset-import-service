package org.veupathdb.service.userds.generated.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "datasetName",
    "description",
    "summary",
    "stepPercent",
    "projects",
    "status",
    "statusDetails",
    "started"
})
public class StatusResponseImpl implements StatusResponse {
  @JsonProperty("id")
  private String id;

  @JsonProperty("datasetName")
  private String datasetName;

  @JsonProperty("description")
  private String description;

  @JsonProperty("summary")
  private String summary;

  @JsonProperty("stepPercent")
  private int stepPercent;

  @JsonProperty("projects")
  private List<String> projects;

  @JsonProperty("status")
  private String status;

  @JsonProperty("statusDetails")
  private StatusResponse.StatusDetailsType statusDetails;

  @JsonProperty("started")
  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
  )
  @JsonDeserialize(
      using = TimestampDeserializer.class
  )
  private Date started;

  @JsonProperty("finished")
  @JsonFormat(
    shape = JsonFormat.Shape.STRING,
    pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
  )
  @JsonDeserialize(
    using = TimestampDeserializer.class
  )
  private Date finished;

  @JsonProperty("id")
  public String getId() {
    return this.id;
  }

  @JsonProperty("id")
  public StatusResponse setId(String id) {
    this.id = id;
    return this;
  }

  @JsonProperty("datasetName")
  public String getDatasetName() {
    return this.datasetName;
  }

  @JsonProperty("datasetName")
  public StatusResponse setDatasetName(String datasetName) {
    this.datasetName = datasetName;
    return this;
  }

  @JsonProperty("description")
  public String getDescription() {
    return this.description;
  }

  @JsonProperty("description")
  public StatusResponse setDescription(String description) {
    this.description = description;
    return this;
  }

  @JsonProperty("summary")
  public String getSummary() {
    return this.summary;
  }

  @JsonProperty("summary")
  public StatusResponse setSummary(String summary) {
    this.summary = summary;
    return this;
  }

  @JsonProperty("stepPercent")
  public int getStepPercent() {
    return this.stepPercent;
  }

  @JsonProperty("stepPercent")
  public StatusResponse setStepPercent(int stepPercent) {
    this.stepPercent = stepPercent;
    return this;
  }

  @JsonProperty("projects")
  public List<String> getProjects() {
    return this.projects;
  }

  @JsonProperty("projects")
  public StatusResponse setProjects(List<String> projects) {
    this.projects = projects;
    return this;
  }

  @JsonProperty("status")
  public String getStatus() {
    return this.status;
  }

  @JsonProperty("status")
  public StatusResponse setStatus(String status) {
    this.status = status;
    return this;
  }

  @JsonProperty("statusDetails")
  public StatusResponse.StatusDetailsType getStatusDetails() {
    return this.statusDetails;
  }

  @JsonProperty("statusDetails")
  public StatusResponse setStatusDetails(StatusResponse.StatusDetailsType statusDetails) {
    this.statusDetails = statusDetails;
    return this;
  }

  @JsonProperty("started")
  public Date getStarted() {
    return this.started;
  }

  @JsonProperty("started")
  public StatusResponse setStarted(Date started) {
    this.started = started;
    return this;
  }

  @JsonProperty("finished")
  public Date getFinished() {
    return this.finished;
  }

  @JsonProperty("finished")
  public StatusResponse setFinished(Date finished) {
    this.finished = finished;
    return this;
  }

  @JsonDeserialize(using = StatusDetailsType.StatusDetailsDeserializer.class)
  @JsonSerialize(using = StatusDetailsType.Serializer.class)
  public static class StatusDetailsTypeImpl implements StatusResponse.StatusDetailsType {
    private Object anyType;

    public StatusDetailsTypeImpl(ValidationErrors validationErrors) {
      this.anyType = validationErrors;
    }

    public StatusDetailsTypeImpl(JobError jobError) {
      this.anyType = jobError;
    }

    public ValidationErrors getValidationErrors() {
      if ( !(anyType instanceof  ValidationErrors)) throw new IllegalStateException("fetching wrong type out of the union: org.veupathdb.service.userds.generated.model.ValidationErrors");
      return (ValidationErrors) anyType;
    }

    public boolean isValidationErrors() {
      return anyType instanceof ValidationErrors;
    }

    public JobError getJobError() {
      if ( !(anyType instanceof  JobError)) throw new IllegalStateException("fetching wrong type out of the union: org.veupathdb.service.userds.generated.model.JobError");
      return (JobError) anyType;
    }

    public boolean isJobError() {
      return anyType instanceof JobError;
    }
  }
}
