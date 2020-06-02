package org.veupathdb.service.userds.repo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.veupathdb.service.userds.model.JobRow;
import org.veupathdb.service.userds.model.JobStatus;
import org.veupathdb.service.userds.model.ProjectCache;
import org.veupathdb.service.userds.model.StatusCache;

abstract class SelectJobBase
{
  protected static JobRow rsToJobRow(ResultSet rs) throws SQLException {

    var projects = new ArrayList <String>(1);
    try (var subRs = rs.getArray(Schema.Column.PROJECTS).getResultSet()) {
      while (subRs.next()) {
        projects.add(ProjectCache.getInstance().getKey(rs.getShort(1)));
      }
    }

    return new JobRow(
      rs.getInt(Schema.Column.DB_ID),
      rs.getString(Schema.Column.JOB_ID),
      rs.getLong(Schema.Column.USER_ID),
      StatusCache.getInstance().getKey(rs.getShort(Schema.Column.STATUS)),
      rs.getString(Schema.Column.NAME),
      rs.getString(Schema.Column.DESCRIPTION),
      rs.getString(Schema.Column.SUMMARY),
      rs.getObject(Schema.Column.STARTED, LocalDateTime.class),
      rs.getObject(Schema.Column.FINISHED, LocalDateTime.class),
      rs.getString(Schema.Column.MESSAGE),
      projects
    );
  }
}
