package org.veupathdb.service.userds.model.handler;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;

public class HandlerPayload
{
  private final String       name;

  private final long          userId;

  private final List <String> projects;

  public HandlerPayload(
    String name,
    long userId,
    List < String > projects
  ) {
    this.name = name;
    this.userId = userId;
    this.projects = projects;
  }

  @JsonGetter("name")
  public String getName() {
    return name;
  }

  @JsonGetter("owner")
  public long getUserId() {
    return userId;
  }

  @JsonGetter("projects")
  public List < String > getProjects() {
    return projects;
  }
}
