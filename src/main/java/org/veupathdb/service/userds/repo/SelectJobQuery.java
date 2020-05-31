package org.veupathdb.service.userds.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

import org.veupathdb.service.userds.model.JobRow;
import org.veupathdb.service.userds.util.DbMan;

/**
 * Select Single Job by Job ID
 *
 * Attempts to find a job entry with the given Job ID value.  Returns an option
 * which will be empty if no such job was found.
 */
public class SelectJobQuery extends SelectJobBase
{
  public static Optional <JobRow> run(String jobId) throws SQLException {
    try (
      var con = DbMan.getImportDb().getConnection();
      var ps  = prepare(con, jobId);
      var rs  = ps.executeQuery()
    ) {
      return rs.next()
        ? Optional.of(rsToJobRow(rs))
        : Optional.empty();
    }
  }

  public static PreparedStatement prepare(Connection con, String jobId)
  throws SQLException {
    final var out = con.prepareStatement(Query.selectSingleJob());
    out.setString(1, jobId);
    return out;

  }
}
