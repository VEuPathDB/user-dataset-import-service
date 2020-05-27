package org.veupathdb.service.userds.model;

public class Service {
  private String dsType;

  private String[] projects;
  private String[] fileTypes;

  // Internal URL to the service, this should be removed when service discovery
  // is enabled.
  private String url;

  public String getDsType() {
    return dsType;
  }

  public void setDsType(String dsType) {
    this.dsType = dsType;
  }

  public String[] getProjects() {
    return projects;
  }

  public void setProjects(String[] projects) {
    this.projects = projects;
  }

  public String[] getFileTypes() {
    return fileTypes;
  }

  public void setFileTypes(String[] fileTypes) {
    this.fileTypes = fileTypes;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
