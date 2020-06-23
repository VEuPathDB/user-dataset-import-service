package org.veupathdb.service.userds.model.handler;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class HandlerPayload
{
  private final String name;

  private final long userId;

  private final List < String > projects;

  private final String summary;

  private final String description;

  public HandlerPayload(
    String name,
    long userId,
    List < String > projects,
    String description,
    String summary
  ) {
    this.name = name;
    this.userId = userId;
    this.projects = projects;
    this.description = description;
    this.summary = summary;
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

  @JsonGetter
  public String getSummary() {
    return summary;
  }

  @JsonGetter
  public String getDescription() {
    return description;
  }
}
