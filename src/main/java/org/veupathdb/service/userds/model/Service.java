package org.veupathdb.service.userds.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Service {
  private String dsType;

  private String[] projects;
  private String[] fileTypes;

  // Internal URL to the service, this should be removed when service discovery
  // is enabled.
  private String url;

  @JsonGetter
  public String getDsType() {
    return dsType;
  }

  @JsonSetter
  public void setDsType(String dsType) {
    this.dsType = dsType;
  }

  @JsonGetter
  public String[] getProjects() {
    return projects;
  }

  @JsonSetter
  public void setProjects(String[] projects) {
    this.projects = projects;
  }

  @JsonGetter
  public String[] getFileTypes() {
    return fileTypes;
  }

  @JsonSetter
  public void setFileTypes(String[] fileTypes) {
    this.fileTypes = fileTypes;
  }

  @JsonGetter
  public String getName() {
    return url;
  }

  @JsonSetter
  public void setName(String url) {
    this.url = url;
  }
}
