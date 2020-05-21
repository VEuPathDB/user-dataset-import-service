package org.veupathdb.service.userds.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Date;
import java.util.List;

@JsonDeserialize(
    as = StatusResponseImpl.class
)
public interface StatusResponse {
  @JsonProperty("id")
  String getId();

  @JsonProperty("id")
  void setId(String id);

  @JsonProperty("dataset")
  String getDataset();

  @JsonProperty("dataset")
  void setDataset(String dataset);

  @JsonProperty("description")
  String getDescription();

  @JsonProperty("description")
  void setDescription(String description);

  @JsonProperty("summary")
  String getSummary();

  @JsonProperty("summary")
  void setSummary(String summary);

  @JsonProperty("stepPercent")
  int getStepPercent();

  @JsonProperty("stepPercent")
  void setStepPercent(int stepPercent);

  @JsonProperty("projects")
  List<ProjectType> getProjects();

  @JsonProperty("projects")
  void setProjects(List<ProjectType> projects);

  @JsonProperty("status")
  JobStatus getStatus();

  @JsonProperty("status")
  void setStatus(JobStatus status);

  @JsonProperty("message")
  String getMessage();

  @JsonProperty("message")
  void setMessage(String message);

  @JsonProperty("started")
  Date getStarted();

  @JsonProperty("started")
  void setStarted(Date started);
}
