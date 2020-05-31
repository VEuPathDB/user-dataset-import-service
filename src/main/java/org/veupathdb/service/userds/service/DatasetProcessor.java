package org.veupathdb.service.userds.service;

import java.io.InputStream;
import java.util.Optional;

import io.vulpine.lib.iffy.Either;
import org.veupathdb.service.userds.model.JobRow;
import org.veupathdb.service.userds.model.JobStatus;
import org.veupathdb.service.userds.model.handler.HandlerGeneralError;
import org.veupathdb.service.userds.model.handler.HandlerValidationError;
import org.veupathdb.service.userds.repo.UpdateJobStatusQuery;

public class DatasetProcessor
{
  private final JobRow      job;
  private final String      fileName;
  private final InputStream reader;

  public DatasetProcessor(
    JobRow job,
    String fileName,
    InputStream reader
  ) {
    this.job      = job;
    this.fileName = fileName;
    this.reader   = reader;
  }

  public Optional< Either < HandlerGeneralError, HandlerValidationError > >
  process() throws Exception {
    final var hand = Handler.getHandler("biom").orElseThrow();

    var res1 = hand.prepareJob(job);
    if (res1.isPresent())
      return res1;

    UpdateJobStatusQuery.run(job.getDbId(), JobStatus.SENDING_TO_HANDLER);
    var res2 = hand.submitJob(job, fileName, reader);
    if (res2.isRight())
      return res2.right();

    var data = res2.leftOrThrow();
    try (var stream = data.getContent()) {
      UpdateJobStatusQuery.run(job.getDbId(), JobStatus.SENDING_TO_DATASTORE);
      Irods.writeDataset(data.getFileName(), stream);
    } catch (Exception e) {
      UpdateJobStatusQuery.run(job.getDbId(), JobStatus.ERRORED);
    }

    UpdateJobStatusQuery.run(job.getDbId(), JobStatus.SUCCESS);
    return Optional.empty();
  }
}
