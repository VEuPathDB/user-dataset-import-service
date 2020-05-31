package org.veupathdb.service.userds.repo;

import java.sql.SQLException;

import org.veupathdb.service.userds.util.DbMan;

public class DeleteJobQuery
{
  public static void run(int dbId) throws SQLException {
    try (
      var cn = DbMan.getImportDb().getConnection();
      var ps = cn.prepareStatement(Query.deleteFile())
    ) {
      ps.setInt(1, dbId);
      ps.execute();
    }
  }
}
