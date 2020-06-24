package org.veupathdb.service.userds.generated.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.veupathdb.service.userds.model.handler.DatasetOrigin;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PrepRequestImpl implements PrepRequest {
  private String datasetName;

  private String description;

  private String summary;

  private List<String> projects;

  private String dsType;

  private DatasetOrigin origin;

  @Override
  @JsonGetter
  public String getDatasetName() {
    return this.datasetName;
  }

  @Override
  @JsonSetter
  public void setDatasetName(String datasetName) {
    this.datasetName = datasetName;
  }

  @Override
  @JsonGetter
  public String getDescription() {
    return this.description;
  }

  @Override
  @JsonSetter
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  @JsonGetter
  public String getSummary() {
    return this.summary;
  }

  @Override
  @JsonSetter
  public void setSummary(String summary) {
    this.summary = summary;
  }

  @Override
  @JsonGetter
  public List<String> getProjects() {
    return this.projects;
  }

  @Override
  @JsonSetter
  public void setProjects(List<String> projects) {
    this.projects = projects;
  }

  @Override
  @JsonGetter
  public String getDatasetType() {
    return dsType;
  }

  @Override
  @JsonSetter
  public void setDatasetType(String dsType) {
    this.dsType = dsType;
  }

  @Override
  @JsonGetter
  public DatasetOrigin getOrigin() {
    return origin;
  }

  @Override
  @JsonSetter
  public void setOrigin(DatasetOrigin origin) {
    this.origin = origin;
  }
}
