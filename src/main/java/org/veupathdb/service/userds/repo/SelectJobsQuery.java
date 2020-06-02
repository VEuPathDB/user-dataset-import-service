package org.veupathdb.service.userds.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.veupathdb.service.userds.model.JobRow;
import org.veupathdb.service.userds.util.DbMan;

/**
 * Select User Jobs
 *
 * Query that returns a list of JobRow instances for the given user ID.  If the
 * user has no jobs, the returned list will be empty.
 */
public class SelectJobsQuery extends SelectJobBase
{
  public static List <JobRow> run(long userId) throws Exception {
    var out = new ArrayList<JobRow>(10);

    try (
      var con = DbMan.getImportDb().getConnection();
      var ps  = prepareStatement(con, userId);
      var rs  = ps.executeQuery()
    ) {
      while (rs.next())
        out.add(rsToJobRow(rs));
    }

    return out;
  }

  private static PreparedStatement prepareStatement(Connection con, long userId)
  throws SQLException {
    final var out = con.prepareStatement(Query.selectJobList());
    out.setLong(1, userId);
    return out;
  }
}
