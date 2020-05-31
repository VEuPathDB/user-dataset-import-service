package org.veupathdb.service.userds.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.veupathdb.service.userds.model.JobRow;
import org.veupathdb.service.userds.model.ProjectCache;
import org.veupathdb.service.userds.model.StatusCache;
import org.veupathdb.service.userds.util.DbMan;

public class InsertJobQuery
{
  public static void run(JobRow row) throws SQLException {
    try (
      var cn = DbMan.getImportDb().getConnection();
      var ps = prepare(cn, row)
    ) {
      ps.execute();
    }
  }

  public static PreparedStatement prepare(Connection cn, JobRow row)
  throws SQLException {
    var out = cn.prepareStatement(Query.insertJob());

    out.setString(1, row.getJobId());
    out.setLong(2, row.getUserId());
    out.setShort(3, StatusCache.getInstance().get(row.getStatus()));
    out.setString(4, row.getName());
    out.setString(5, row.getDescription().orElse(null));
    out.setString(6, row.getSummary().orElse(null));

    out.setArray(7, cn.createArrayOf("SMALLINT", row.getProjects()
      .stream()
      .map(ProjectCache.getInstance()::get)
      .toArray(Object[]::new)));

    return out;

  }
}
