package org.veupathdb.service.userds.repo;

import java.sql.ResultSet;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.apache.logging.log4j.Logger;
import org.veupathdb.lib.container.jaxrs.providers.LogProvider;
import org.veupathdb.service.userds.model.JobRow;
import org.veupathdb.service.userds.model.ProjectCache;
import org.veupathdb.service.userds.model.StatusCache;
import org.veupathdb.service.userds.util.Format;

abstract class SelectJobBase
{
  private static final Logger log = LogProvider.logger(SelectJobBase.class);

  protected static JobRow rsToJobRow(ResultSet rs) throws Exception {
    log.trace("SelectJobBase#rsToJobRow");

    final var jobId = rs.getString(Schema.Column.JOB_ID);

    log.debug("Building job " + jobId);

    var projects = new ArrayList <String>(1);
    try (var subRs = rs.getArray(Schema.Column.PROJECTS).getResultSet()) {
      while (subRs.next()) {
        var projectId = rs.getShort(2);
        var project   = ProjectCache.getInstance().getKey(projectId);

        log.debug("Appending project {} ({})", project, projectId);

        projects.add(project);
      }
    }

    var messageStr = rs.getString(Schema.Column.MESSAGE);
    var message = messageStr != null
      ? Format.Json.readTree(messageStr)
      : null;

    return new JobRow(
      rs.getInt(Schema.Column.DB_ID),
      jobId,
      rs.getLong(Schema.Column.USER_ID),
      StatusCache.getInstance().getKey(rs.getShort(Schema.Column.STATUS)),
      rs.getString(Schema.Column.NAME),
      rs.getString(Schema.Column.DESCRIPTION),
      rs.getString(Schema.Column.SUMMARY),
      rs.getObject(Schema.Column.STARTED, OffsetDateTime.class)
        .toLocalDateTime(),
      Optional.ofNullable(
        rs.getObject(Schema.Column.FINISHED, OffsetDateTime.class))
          .map(OffsetDateTime::toLocalDateTime)
        .orElse(null),
      message,
      projects
    );
  }
}
