package org.veupathdb.service.userds.generated.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.veupathdb.service.userds.model.handler.DatasetOrigin;

import java.util.List;

@JsonDeserialize(as = PrepRequestImpl.class)
public interface PrepRequest
{
  String
    KEY_DS_NAME  = "datasetName",
    KEY_DESC     = "description",
    KEY_SUMMARY  = "summary",
    KEY_PROJECTS = "projects",
    KEY_DS_TYPE  = "datasetType",
    KEY_ORIGIN   = "datasetOrigin";

  @JsonProperty(KEY_DS_NAME)
  String getDatasetName();

  @JsonGetter
  DatasetOrigin getOrigin();

  @JsonProperty(KEY_DS_NAME)
  void setDatasetName(String datasetName);

  @JsonProperty(KEY_DESC)
  String getDescription();

  @JsonProperty(KEY_DESC)
  void setDescription(String description);

  @JsonProperty(KEY_SUMMARY)
  String getSummary();

  @JsonProperty(KEY_SUMMARY)
  void setSummary(String summary);

  @JsonProperty(KEY_PROJECTS)
  List < String > getProjects();

  @JsonProperty(KEY_PROJECTS)
  void setProjects(List < String > projects);

  @JsonProperty(KEY_DS_TYPE)
  String getDatasetType();

  @JsonProperty(KEY_DS_TYPE)
  void setDatasetType(String dsType);


  @JsonSetter
  void setOrigin(DatasetOrigin origin);
}
