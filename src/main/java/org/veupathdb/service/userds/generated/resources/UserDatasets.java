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
  GetResponse getUserDatasets();

  @POST
  @Produces("application/json")
  @Consumes("application/json")
  PostResponse postUserDatasets(PrepRequest entity);

  @GET
  @Path("/{jobId}")
  @Produces("application/json")
  GetByJobIdResponse getUserDatasetsByJobId(@PathParam("jobId") String jobId);

  @POST
  @Path("/{jobId}")
  @Produces("application/json")
  @Consumes("multipart/form-data")
  PostByJobIdResponse postUserDatasetsByJobId(
    @PathParam("jobId")    String jobId,
    InputStream body
  );

  class GetResponse extends ResponseDelegate {
    private GetResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetResponse(Response response) {
      super(response);
    }

    public static GetResponse respond200(
        List<StatusResponse> entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      GenericEntity<List<StatusResponse>> wrappedEntity = new GenericEntity<List<StatusResponse>>(entity){};
      responseBuilder.entity(wrappedEntity);
      return new GetResponse(responseBuilder.build(), wrappedEntity);
    }

    public static GetResponse respond401WithApplicationJson(UnauthorizedError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(401).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetResponse(responseBuilder.build(), entity);
    }

    public static GetResponse respond500(ServerError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(500).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetResponse(responseBuilder.build(), entity);
    }
  }

  class PostResponse extends ResponseDelegate {
    private PostResponse(Response response, Object entity) {
      super(response, entity);
    }

    private PostResponse(Response response) {
      super(response);
    }

    public static PostResponse respond200(PrepResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostResponse(responseBuilder.build(), entity);
    }

    public static PostResponse respond400WithApplicationJson(BadRequestError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(400).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostResponse(responseBuilder.build(), entity);
    }

    public static PostResponse respond401WithApplicationJson(UnauthorizedError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(401).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostResponse(responseBuilder.build(), entity);
    }

    public static PostResponse respond422(InvalidInputError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(422).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostResponse(responseBuilder.build(), entity);
    }

    public static PostResponse respond500(ServerError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(500).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostResponse(responseBuilder.build(), entity);
    }
  }

  class GetByJobIdResponse extends ResponseDelegate {
    private GetByJobIdResponse(Response response, Object entity) {
      super(response, entity);
    }

    private GetByJobIdResponse(Response response) {
      super(response);
    }

    public static GetByJobIdResponse respond200(
        StatusResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetByJobIdResponse(responseBuilder.build(), entity);
    }

    public static GetByJobIdResponse respond401WithApplicationJson(
        UnauthorizedError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(401).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetByJobIdResponse(responseBuilder.build(), entity);
    }

    public static GetByJobIdResponse respond404(
        NotFoundError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(404).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetByJobIdResponse(responseBuilder.build(), entity);
    }

    public static GetByJobIdResponse respond500(ServerError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(500).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new GetByJobIdResponse(responseBuilder.build(), entity);
    }
  }

  class PostByJobIdResponse extends ResponseDelegate {
    private PostByJobIdResponse(Response response, Object entity) {
      super(response, entity);
    }

    public static PostByJobIdResponse respond200(
        ProcessResponse entity) {
      Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostByJobIdResponse(responseBuilder.build(), entity);
    }

    public static PostByJobIdResponse respond404(
        NotFoundError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(404).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostByJobIdResponse(responseBuilder.build(), entity);
    }

    public static PostByJobIdResponse respond500(
        ServerError entity) {
      Response.ResponseBuilder responseBuilder = Response.status(500).header("Content-Type", "application/json");
      responseBuilder.entity(entity);
      return new PostByJobIdResponse(responseBuilder.build(), entity);
    }
  }
}
