package org.veupathdb.service.userds.controller;

import java.util.Map;
import java.util.Set;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.veupathdb.service.userds.Main;

@Path("/debug")
public class DebugController
{
  @GET
  @Path("/config/projects-by-ds-type")
  @Produces(MediaType.APPLICATION_JSON)
  public Map <String, Set <String> > getProjectsByDsType() {
    return Main.jsonConfig.getByType();
  }
}
