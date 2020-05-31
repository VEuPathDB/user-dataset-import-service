package org.veupathdb.service.userds.service;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.*;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.JsonNode;
import io.vulpine.lib.iffy.Either;
import org.apache.logging.log4j.Logger;
import org.veupathdb.lib.container.jaxrs.providers.LogProvider;
import org.veupathdb.service.userds.Main;
import org.veupathdb.service.userds.model.handler.HandlerPayload;
import org.veupathdb.service.userds.model.JobRow;
import org.veupathdb.service.userds.model.JobStatus;
import org.veupathdb.service.userds.model.Service;
import org.veupathdb.service.userds.model.handler.HandlerGeneralError;
import org.veupathdb.service.userds.model.handler.HandlerJobResult;
import org.veupathdb.service.userds.model.handler.HandlerValidationError;
import org.veupathdb.service.userds.repo.InsertMessageQuery;
import org.veupathdb.service.userds.repo.UpdateJobStatusQuery;
import org.veupathdb.service.userds.util.Errors;
import org.veupathdb.service.userds.util.http.Header;
import org.veupathdb.service.userds.util.http.MultipartBodyPublisher;

import static java.lang.String.format;
import static org.veupathdb.service.userds.util.Format.Json;

public class Handler
{
  private static final String
    jobEndpoint    = "http://%s/job/%s",
    statusEndpoint = jobEndpoint + "/status",
    boundary       = "DATASET-CONTENT",
    MULTIPART_HEAD = MediaType.MULTIPART_FORM_DATA + "; boundary=" + boundary,
    fileName       = "filename=";

  private static final Map < String, Handler > handlers = Collections
    .synchronizedMap(new HashMap <>());

  private static final Logger logger = LogProvider.logger(Handler.class);

  private final Service svc;

  public Handler(Service svc) {
    this.svc = svc;
  }

  // TODO: This should return something useful.
  public Optional < Either < HandlerGeneralError, HandlerValidationError > >
  prepareJob(JobRow job) throws Exception {

    var res = HttpClient.newHttpClient()
      .send(
        HttpRequest.newBuilder(
          URI.create(
            format(jobEndpoint, svc.getUrl(), job.getJobId())
          )
        )
          .PUT(
            BodyPublishers.ofString(
              Json.writeValueAsString(
                new HandlerPayload(
                  job.getName(),
                  job.getUserId(),
                  job.getProjects()
                )
              )
            )
          )
          .header(Header.CONTENT_TYPE, MediaType.APPLICATION_JSON)
          .build(),
        BodyHandlers.ofString()
      );

    if (res.statusCode() == 204) {
      UpdateJobStatusQuery.run(job.getDbId(), JobStatus.HANDLER_PROCESSING);
      return Optional.empty();
    }

    if (res.statusCode() == 422) {
      final var raw = Json.readTree(res.body());
      return Optional.of(Either.ofRight(do422(job.getDbId(), raw)));
    }

    final var msg = Json.readValue(res.body(), HandlerGeneralError.class);

    return Optional.of(Either.ofLeft(
      switch (res.statusCode()) {
        case 400 -> do400(job.getDbId(), msg);
        default -> do500(job.getDbId(), msg);
      }
    ));
  }

  public Either < HandlerJobResult, Either < HandlerGeneralError, HandlerValidationError > >
  submitJob(
    final JobRow job,
    final String fileName,
    final InputStream body
  ) throws Exception {
    try {
      final var res = HttpClient.newHttpClient().send(
        HttpRequest.newBuilder(URI.create(
          format(jobEndpoint, svc.getUrl(), job.getJobId())))
          .header(Header.CONTENT_TYPE, MULTIPART_HEAD)
          .POST(MultipartBodyPublisher.publish(fileName, body, boundary))
          .build(),
        BodyHandlers.ofInputStream()
      );
      Errors.swallow(body::close);

      if (res.statusCode() == 200) {
        var optName = res.headers()
          .allValues(Header.CONTENT_DISPOSITION)
          .stream()
          .filter(v -> v.startsWith(Handler.fileName))
          .findAny()
          .map(v -> v.substring(Handler.fileName.length()));

        if (optName.isEmpty()) {
          var msg = "invalid response from handler:  no filename header.";
          logger.error(msg);
          throw new Exception(msg);
        }

        return Either.ofLeft(new HandlerJobResult(optName.get(), res.body()));
      }

      if (res.statusCode() == 422) {
        return Either.ofRight(Either.ofRight(do422(
          job.getDbId(),
          Json.readTree(res.body())
        )));
      }

      final var err = Json.readValue(res.body(), HandlerGeneralError.class);

      return Either.ofRight(Either.ofLeft(
        switch (res.statusCode()) {
          case 400 -> do400(job.getDbId(), err);
          default -> do500(job.getDbId(), err);
        }
      ));
    } finally {
      Errors.swallow(body::close);
    }
  }

  public static Optional < Handler > getHandler(String format) {
    var form = format.toLowerCase();

    if (handlers.containsKey(form))
      return Optional.ofNullable(handlers.get(form));

    var optSvc = Arrays.stream(Main.jsonConfig.getServices())
      .filter(svc -> form.equals(svc.getDsType()))
      .findAny();

    if (optSvc.isEmpty()) {
      handlers.put(form, null);
      return Optional.empty();
    }

    var out = new Handler(optSvc.get());
    handlers.put(form, out);
    return Optional.of(out);
  }

  private HandlerGeneralError do400(int dbId, HandlerGeneralError err)
  throws Exception {
    UpdateJobStatusQuery.run(dbId, JobStatus.REJECTED);
    InsertMessageQuery.run(dbId, err.getMessage());

    return err;
  }

  private HandlerValidationError do422(int dbId, JsonNode err)
  throws Exception {
    UpdateJobStatusQuery.run(dbId, JobStatus.ERRORED);
    InsertMessageQuery.run(dbId, err.get(HandlerValidationError.KEY_ERRORS));

    return Json.convertValue(err, HandlerValidationError.class);
  }

  private HandlerGeneralError do500(int dbId, HandlerGeneralError err)
  throws Exception {
    do500(dbId, err.getMessage());
    return err;
  }

  private void do500(int dbId, String err) throws Exception {
    UpdateJobStatusQuery.run(dbId, JobStatus.ERRORED);
    InsertMessageQuery.run(dbId, err);
  }
}
