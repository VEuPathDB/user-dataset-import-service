package org.veupathdb.service.userds.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.veupathdb.service.userds.util.DbMan;

/**
 * Delete Single Job by Job ID
 */
public class DeleteJobQuery
{
  public static void run(int dbId) throws Exception {
    try (
      var con = DbMan.getImportDb().getConnection();
      var ps  = prepare(con, dbId);
    ) {
      ps.execute();
    }
  }

  public static PreparedStatement prepare(Connection con, int dbId)
  throws SQLException {
    final var out = con.prepareStatement(SQL.Delete.Job);
    out.setInt(1, dbId);
    return out;
  }
}
