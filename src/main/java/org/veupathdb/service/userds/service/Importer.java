package org.veupathdb.service.userds.service;

import java.io.InputStream;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.veupathdb.lib.container.jaxrs.providers.LogProvider;
import org.veupathdb.service.userds.model.JobRow;
import org.veupathdb.service.userds.model.JobStatus;
import org.veupathdb.service.userds.model.handler.HandlerGeneralError;
import org.veupathdb.service.userds.model.handler.HandlerJobResult;
import org.veupathdb.service.userds.model.handler.HandlerValidationError;
import org.veupathdb.service.userds.repo.InsertMessageQuery;
import org.veupathdb.service.userds.repo.UpdateJobStatusQuery;
import org.veupathdb.service.userds.util.Errors;

import static org.veupathdb.service.userds.util.Format.Json;

public class Importer implements Runnable
{
  private final JobRow      job;
  private final InputStream reader;

  public Importer(
    JobRow job,
    InputStream reader
  ) {
    this.job = job;
    this.reader = reader;
  }

  public void run() {
    try {
      final var hand = Handler.getHandler("biom").orElseThrow();

      if (!doPrep(hand))
        return;

      UpdateJobStatusQuery.run(job.getDbId(), JobStatus.SENDING_TO_HANDLER);

      var result = doSubmit(hand);

      if (result.isEmpty())
        return;

      try {
        doStore(result.get());
      } finally {
        Errors.swallow(() -> result.get().getContent().close());
      }

      UpdateJobStatusQuery.run(job.getDbId(), JobStatus.SUCCESS);
    } catch (Throwable e) {
      LogProvider.logger(getClass())
        .error("Failed to submit job to handler", e);
      Errors.swallow(() ->
        UpdateJobStatusQuery.run(job.getDbId(), JobStatus.ERRORED));
      Errors.swallow(() ->
        InsertMessageQuery.run(job.getDbId(), e.getMessage()));
    } finally {
      Errors.swallow(reader::close);
    }
  }

  private boolean doPrep(Handler hand) throws Exception {
    var raw = hand.prepareJob(job);

    if (raw.isEmpty())
      return true;

    var res = raw.get();

    if (res.isLeft()) {
      var val = res.leftOrThrow();
      switch (val.getCode()) {
        case 400, 401 -> do400(val);
        default -> do500(val);
      }
      return false;
    }

    do422(res.rightOrThrow());
    return false;
  }

  private Optional< HandlerJobResult > doSubmit(Handler hand) throws Exception {
    var raw = hand.submitJob(job, reader);

    if (raw.isLeft())
      return Optional.of(raw.leftOrThrow());

    var errs = raw.rightOrThrow();

    if (errs.isLeft()) {
      var val = errs.leftOrThrow();
      switch (val.getCode()) {
        case 400, 401 -> do400(val);
        default -> do500(val);
      }
    } else {
      do422(errs.rightOrThrow());
    }

    return Optional.empty();
  }

  private void doStore(HandlerJobResult result) throws Exception {
    LogProvider.logger(getClass()).trace("Importer#doStore");
    Irods.writeDataset(result.getFileName(), result.getContent());
    result.getContent().close();
  }

  private void do400(HandlerGeneralError err) throws Exception {
    LogProvider.logger(getClass()).trace("Importer#do400");
    UpdateJobStatusQuery.run(job.getDbId(), JobStatus.REJECTED);
    InsertMessageQuery.run(job.getDbId(), err.getMessage());
  }

  private void do422(HandlerValidationError err) throws Exception {
    LogProvider.logger(getClass()).trace("Importer#do400");
    var js = Json.convertValue(err.getErrors(), JsonNode.class);
    UpdateJobStatusQuery.run(job.getDbId(), JobStatus.ERRORED);
    InsertMessageQuery.run(job.getDbId(), js);
  }

  private void do500(HandlerGeneralError err) throws Exception {
    do500(err.getMessage());
  }

  private void do500(String err) throws Exception {
    LogProvider.logger(getClass()).trace("Importer#do500");
    UpdateJobStatusQuery.run(job.getDbId(), JobStatus.ERRORED);
    InsertMessageQuery.run(job.getDbId(), err);
  }
}
