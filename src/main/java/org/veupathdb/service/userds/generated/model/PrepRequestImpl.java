package org.veupathdb.service.userds.generated.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PrepRequestImpl implements PrepRequest {
  private String datasetName;

  private String description;

  private String summary;

  private List<String> projects;

  private String dsType;

  @JsonGetter
  public String getDatasetName() {
    return this.datasetName;
  }

  @JsonSetter
  public void setDatasetName(String datasetName) {
    this.datasetName = datasetName;
  }

  @JsonGetter
  public String getDescription() {
    return this.description;
  }

  @JsonSetter
  public void setDescription(String description) {
    this.description = description;
  }

  @JsonGetter
  public String getSummary() {
    return this.summary;
  }

  @JsonSetter
  public void setSummary(String summary) {
    this.summary = summary;
  }

  @JsonGetter
  public List<String> getProjects() {
    return this.projects;
  }

  @JsonSetter
  public void setProjects(List<String> projects) {
    this.projects = projects;
  }

  @JsonGetter
  public String getDatasetType() {
    return dsType;
  }

  @JsonSetter
  public void setDatasetType(String dsType) {
    this.dsType = dsType;
  }
}
