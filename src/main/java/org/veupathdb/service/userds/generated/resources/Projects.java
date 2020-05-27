package org.veupathdb.service.userds.generated.resources;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import org.veupathdb.service.userds.generated.model.ProjectType;
import org.veupathdb.service.userds.generated.support.ResponseDelegate;

@Path("/projects")
public interface Projects {
  @GET
  @Produces("application/json")
  GetProjectsResponse getProjects();

  @GET
  @Path("/{projectType}/datasetTypes")
  @Produces("application/json")
  GetProjectsDatasetTypesByProjectTypeResponse getProjectsDatasetTypesByProjectType(
      @PathParam("projectType") ProjectType projectType);

  @GET
  @Path("/{projectType}/datasetTypes/{datasetType}/fileTypes")
  @Produces("application/json")
  GetProjectsDatasetTypesFileTypesByProjectTypeAndDatasetTypeResponse getProjectsDatasetTypesFileTypesByProjectTypeAndDatasetType(
      @PathParam("projectType") ProjectType projectType,
      @PathParam("datasetType") String datasetType);

  class GetProjectsResponse extends ResponseDelegate {
    private GetProjectsResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetProjectsResponse(Response response) {
      super(response);
    }

    public static GetProjectsResponse respond200WithApplicationJson(List<String> entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      GenericEntity<List<String>> wrappedEntity = new GenericEntity<List<String>>(entity){};
      responseBuilder.entity(wrappedEntity);
      return new GetProjectsResponse(responseBuilder.build(), wrappedEntity);
    }
  }

  class GetProjectsDatasetTypesByProjectTypeResponse extends ResponseDelegate {
    private GetProjectsDatasetTypesByProjectTypeResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetProjectsDatasetTypesByProjectTypeResponse(Response response) {
      super(response);
    }

    public static GetProjectsDatasetTypesByProjectTypeResponse respond200WithApplicationJson(
        List<Object> entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      GenericEntity<List<Object>> wrappedEntity = new GenericEntity<List<Object>>(entity){};
      responseBuilder.entity(wrappedEntity);
      return new GetProjectsDatasetTypesByProjectTypeResponse(responseBuilder.build(), wrappedEntity);
    }
  }

  class GetProjectsDatasetTypesFileTypesByProjectTypeAndDatasetTypeResponse extends ResponseDelegate {
    private GetProjectsDatasetTypesFileTypesByProjectTypeAndDatasetTypeResponse(Response response,
        Object entity) {
      super(response, entity);
    }

    private GetProjectsDatasetTypesFileTypesByProjectTypeAndDatasetTypeResponse(Response response) {
      super(response);
    }

    public static GetProjectsDatasetTypesFileTypesByProjectTypeAndDatasetTypeResponse respond200WithApplicationJson(
        List<Object> entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      GenericEntity<List<Object>> wrappedEntity = new GenericEntity<List<Object>>(entity){};
      responseBuilder.entity(wrappedEntity);
      return new GetProjectsDatasetTypesFileTypesByProjectTypeAndDatasetTypeResponse(responseBuilder.build(), wrappedEntity);
    }
  }
}
