package org.veupathdb.service.userds.repo;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.veupathdb.service.userds.model.JobStatus;
import org.veupathdb.service.userds.model.StatusCache;
import org.veupathdb.service.userds.util.DbMan;

/**
 * Query to update the completion date of a single job row.
 */
public class UpdateJobCompletedQuery
{
  public static void run(int dbId, LocalDateTime time) throws SQLException {
    try (
      var cn = DbMan.getImportDb().getConnection();
      var ps = cn.prepareStatement(Query.updateJobCompleted())
    ) {
      ps.setObject(1, time.atOffset(ZoneOffset.UTC));
      ps.setInt(2, dbId);
      ps.execute();
    }
  }
}
