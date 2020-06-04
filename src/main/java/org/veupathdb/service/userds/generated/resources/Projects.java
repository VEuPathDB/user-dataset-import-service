package org.veupathdb.service.userds.generated.resources;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

import org.veupathdb.service.userds.generated.model.NotFoundError;
import org.veupathdb.service.userds.generated.support.ResponseDelegate;

@Path("/projects")
public interface Projects
{
  @GET
  @Produces("application/json")
  GetProjectsResponse getProjects();

  @GET
  @Path("/{project}/datasetTypes")
  @Produces("application/json")
  GetProjectHandlers getProjectHandlers(@PathParam("project") String project);

  @GET
  @Path("/{project}/datasetTypes/{dsType}/fileTypes")
  @Produces("application/json")
  GetHandlerFileTypes getHandlerFileTypes(
    @PathParam("project") String project,
    @PathParam("dsType") String dsType
  );

  class GetProjectsResponse extends ResponseDelegate
  {
    private GetProjectsResponse(Response response, Object entity) {
      super(response, entity);
    }

    public static GetProjectsResponse respond200(List < String > entity) {
      var responseBuilder = Response.status(200)
        .header("Content-Type", "application/json");
      var wrappedEntity = new GenericEntity <>(entity){};
      responseBuilder.entity(wrappedEntity);
      return new GetProjectsResponse(responseBuilder.build(), wrappedEntity);
    }
  }

  class GetProjectHandlers extends ResponseDelegate
  {
    private GetProjectHandlers(Response response, Object entity) {
      super(response, entity);
    }

    public static GetProjectHandlers respond200(List < Object > entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200)
        .header("Content-Type", "application/json");
      GenericEntity < List < Object > > wrappedEntity = new GenericEntity <>(
        entity)
      {
      };
      responseBuilder.entity(wrappedEntity);
      return new GetProjectHandlers(
        responseBuilder.build(),
        wrappedEntity
      );
    }

    public static GetProjectHandlers respond404(NotFoundError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(404)
        .header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetProjectHandlers(responseBuilder.build(), entity);
    }
  }

  class GetHandlerFileTypes extends ResponseDelegate
  {
    private GetHandlerFileTypes(Response response, Object entity) {
      super(response, entity);
    }

    public static GetHandlerFileTypes respond200(List < Object > entity) {
      var responseBuilder = Response.status(200)
        .header("Content-Type", "application/json");
      var wrappedEntity = new GenericEntity <>(entity) {};
      responseBuilder.entity(wrappedEntity);
      return new GetHandlerFileTypes(responseBuilder.build(), wrappedEntity);
    }

    public static GetHandlerFileTypes respond404(NotFoundError entity) {
      var responseBuilder = Response.status(404)
        .header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetHandlerFileTypes(responseBuilder.build(), entity);
    }
  }
}
