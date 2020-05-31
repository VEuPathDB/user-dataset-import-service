package org.veupathdb.service.userds.repo;

import java.sql.SQLException;

import org.veupathdb.service.userds.model.JobStatus;
import org.veupathdb.service.userds.model.StatusCache;
import org.veupathdb.service.userds.util.DbMan;

public class UpdateJobStatusQuery
{
  public static void run(int dbId, JobStatus status) throws SQLException {
    try (
      var cn = DbMan.getImportDb().getConnection();
      var ps = cn.prepareStatement(Query.updateJobStatus())
    ) {
      ps.setShort(1, StatusCache.getInstance().get(status));
      ps.setInt(2, dbId);
      ps.execute();
    }
  }
}
