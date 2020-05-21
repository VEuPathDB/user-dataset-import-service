package org.veupathdb.service.userds.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "datasetName",
    "description",
    "summary",
    "projects"
})
public class PrepRequestImpl implements PrepRequest {
  @JsonProperty("datasetName")
  private String datasetName;

  @JsonProperty("description")
  private String description;

  @JsonProperty("summary")
  private String summary;

  @JsonProperty("projects")
  private List<ProjectType> projects;

  @JsonProperty("datasetName")
  public String getDatasetName() {
    return this.datasetName;
  }

  @JsonProperty("datasetName")
  public void setDatasetName(String datasetName) {
    this.datasetName = datasetName;
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

  @JsonProperty("projects")
  public List<ProjectType> getProjects() {
    return this.projects;
  }

  @JsonProperty("projects")
  public void setProjects(List<ProjectType> projects) {
    this.projects = projects;
  }
}
