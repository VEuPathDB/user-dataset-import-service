package org.veupathdb.service.userds.generated.resources;

import java.io.File;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import org.veupathdb.service.userds.generated.model.BadRequestError;
import org.veupathdb.service.userds.generated.model.InvalidInputError;
import org.veupathdb.service.userds.generated.model.PrepRequest;
import org.veupathdb.service.userds.generated.model.PrepResponse;
import org.veupathdb.service.userds.generated.model.ProcessResponse;
import org.veupathdb.service.userds.generated.model.ServerError;
import org.veupathdb.service.userds.generated.model.StatusResponse;
import org.veupathdb.service.userds.generated.model.UnauthorizedError;
import org.veupathdb.service.userds.generated.support.ResponseDelegate;

@Path("/user-datasets")
public interface UserDatasets {
  @GET
  @Produces("application/json")
  GetUserDatasetsResponse getUserDatasets();

  @POST
  @Produces("application/json")
  @Consumes("application/json")
  PostUserDatasetsResponse postUserDatasets(PrepRequest entity);

  @GET
  @Path("/{processId}")
  @Produces("application/json")
  GetUserDatasetsByProcessIdResponse getUserDatasetsByProcessId(
      @PathParam("processId") String processId);

  @POST
  @Path("/{processId}")
  @Produces("application/json")
  @Consumes("application/binary")
  PostUserDatasetsByProcessIdResponse postUserDatasetsByProcessId(
      @PathParam("processId") String processId, File entity);

  class GetUserDatasetsResponse extends ResponseDelegate {
    private GetUserDatasetsResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetUserDatasetsResponse(Response response) {
      super(response);
    }

    public static GetUserDatasetsResponse respond200WithApplicationJson(
        List<StatusResponse> entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      GenericEntity<List<StatusResponse>> wrappedEntity = new GenericEntity<List<StatusResponse>>(entity){};
      responseBuilder.entity(wrappedEntity);
      return new GetUserDatasetsResponse(responseBuilder.build(), wrappedEntity);
    }

    public static GetUserDatasetsResponse respond401WithApplicationJson(UnauthorizedError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(401).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetUserDatasetsResponse(responseBuilder.build(), entity);
    }

    public static GetUserDatasetsResponse respond500WithApplicationJson(ServerError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(500).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetUserDatasetsResponse(responseBuilder.build(), entity);
    }
  }

  class PostUserDatasetsResponse extends ResponseDelegate {
    private PostUserDatasetsResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostUserDatasetsResponse(Response response) {
      super(response);
    }

    public static PostUserDatasetsResponse respond200WithApplicationJson(PrepResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostUserDatasetsResponse(responseBuilder.build(), entity);
    }

    public static PostUserDatasetsResponse respond400WithApplicationJson(BadRequestError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(400).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostUserDatasetsResponse(responseBuilder.build(), entity);
    }

    public static PostUserDatasetsResponse respond401WithApplicationJson(UnauthorizedError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(401).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostUserDatasetsResponse(responseBuilder.build(), entity);
    }

    public static PostUserDatasetsResponse respond422WithApplicationJson(InvalidInputError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(422).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostUserDatasetsResponse(responseBuilder.build(), entity);
    }

    public static PostUserDatasetsResponse respond500WithApplicationJson(ServerError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(500).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostUserDatasetsResponse(responseBuilder.build(), entity);
    }
  }

  class PostUserDatasetsByProcessIdResponse extends ResponseDelegate {
    private PostUserDatasetsByProcessIdResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostUserDatasetsByProcessIdResponse(Response response) {
      super(response);
    }

    public static PostUserDatasetsByProcessIdResponse respond200WithApplicationJson(
        ProcessResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostUserDatasetsByProcessIdResponse(responseBuilder.build(), entity);
    }

    public static PostUserDatasetsByProcessIdResponse respond400WithApplicationJson(
        BadRequestError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(400).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostUserDatasetsByProcessIdResponse(responseBuilder.build(), entity);
    }

    public static PostUserDatasetsByProcessIdResponse respond401WithApplicationJson(
        UnauthorizedError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(401).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostUserDatasetsByProcessIdResponse(responseBuilder.build(), entity);
    }

    public static PostUserDatasetsByProcessIdResponse respond422WithApplicationJson(
        InvalidInputError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(422).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostUserDatasetsByProcessIdResponse(responseBuilder.build(), entity);
    }

    public static PostUserDatasetsByProcessIdResponse respond500WithApplicationJson(
        ServerError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(500).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostUserDatasetsByProcessIdResponse(responseBuilder.build(), entity);
    }
  }

  class GetUserDatasetsByProcessIdResponse extends ResponseDelegate {
    private GetUserDatasetsByProcessIdResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetUserDatasetsByProcessIdResponse(Response response) {
      super(response);
    }

    public static GetUserDatasetsByProcessIdResponse respond200WithApplicationJson(
        StatusResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetUserDatasetsByProcessIdResponse(responseBuilder.build(), entity);
    }

    public static GetUserDatasetsByProcessIdResponse respond401WithApplicationJson(
        UnauthorizedError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(401).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetUserDatasetsByProcessIdResponse(responseBuilder.build(), entity);
    }

    public static GetUserDatasetsByProcessIdResponse respond500WithApplicationJson(
        ServerError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(500).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetUserDatasetsByProcessIdResponse(responseBuilder.build(), entity);
    }
  }
}
