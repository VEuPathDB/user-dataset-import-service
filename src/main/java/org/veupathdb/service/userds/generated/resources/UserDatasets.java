package org.veupathdb.service.userds.generated.resources;

import java.io.InputStream;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

import org.veupathdb.service.userds.generated.model.*;
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
  @Path("/{jobId}")
  @Produces("application/json")
  GetUserDatasetsByJobIdResponse getUserDatasetsByJobId(@PathParam("jobId") String jobId);

  @POST
  @Path("/{jobId}")
  @Produces("application/json")
  @Consumes("multipart/form-data")
  PostUserDatasetsByJobIdResponse postUserDatasetsByJobId(
    @PathParam("jobId")    String jobId,
    InputStream body
  );

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

  class GetUserDatasetsByJobIdResponse extends ResponseDelegate {
    private GetUserDatasetsByJobIdResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetUserDatasetsByJobIdResponse(Response response) {
      super(response);
    }

    public static GetUserDatasetsByJobIdResponse respond200WithApplicationJson(
        StatusResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetUserDatasetsByJobIdResponse(responseBuilder.build(), entity);
    }

    public static GetUserDatasetsByJobIdResponse respond401WithApplicationJson(
        UnauthorizedError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(401).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetUserDatasetsByJobIdResponse(responseBuilder.build(), entity);
    }

    public static GetUserDatasetsByJobIdResponse respond404WithApplicationJson(
        NotFoundError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(404).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetUserDatasetsByJobIdResponse(responseBuilder.build(), entity);
    }

    public static GetUserDatasetsByJobIdResponse respond500WithApplicationJson(ServerError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(500).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetUserDatasetsByJobIdResponse(responseBuilder.build(), entity);
    }
  }

  class PostUserDatasetsByJobIdResponse extends ResponseDelegate {
    private PostUserDatasetsByJobIdResponse(Response response, Object entity) {
      super(response, entity);
    }

    public static PostUserDatasetsByJobIdResponse respond200WithApplicationJson(
        ProcessResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostUserDatasetsByJobIdResponse(responseBuilder.build(), entity);
    }

    public static PostUserDatasetsByJobIdResponse respond404WithApplicationJson(
        NotFoundError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(404).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostUserDatasetsByJobIdResponse(responseBuilder.build(), entity);
    }

    public static PostUserDatasetsByJobIdResponse respond500WithApplicationJson(
        ServerError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(500).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostUserDatasetsByJobIdResponse(responseBuilder.build(), entity);
    }
  }
}
