package org.veupathdb.service.userds.controller;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.veupathdb.service.userds.model.ProjectCache;
import org.veupathdb.service.userds.model.StatusCache;

@Path("debug")
public class DebugSvc
{
  @GET
  @Path("projects")
  @Produces(MediaType.APPLICATION_JSON)
  public Map < String, Map < ?, ? > > getProjects() {
    final var projects = ProjectCache.getInstance();
    final var out      = new HashMap < String, Map < ?, ? > >();
    out.put("idByValue", projects);
    out.put("valueById", projects.getInverse());
    return out;
  }

  @GET
  @Path("statuses")
  @Produces(MediaType.APPLICATION_JSON)
  public Map < String, Map < ?, ? > > getStatuses() {
    final var statuses = StatusCache.getInstance();
    final var out      = new HashMap < String, Map < ?, ? > >();
    out.put("idByValue", statuses);
    out.put("valueById", statuses.getInverse());
    return out;
  }
}
