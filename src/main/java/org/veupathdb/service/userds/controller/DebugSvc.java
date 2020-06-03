package org.veupathdb.service.userds.controller;

import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.veupathdb.service.userds.model.JobStatus;
import org.veupathdb.service.userds.model.ProjectCache;
import org.veupathdb.service.userds.model.StatusCache;

@Path("debug")
public class DebugSvc
{
  @GET
  @Path("projects")
  public Map < String, Short > getProjects() {
    return ProjectCache.getInstance();
  }

  @GET
  @Path("statuses")
  public Map < JobStatus, Short > getStatuses() {
    return StatusCache.getInstance();
  }
}
