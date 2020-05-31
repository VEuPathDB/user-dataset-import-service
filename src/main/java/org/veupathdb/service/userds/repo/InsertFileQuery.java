package org.veupathdb.service.userds.repo;

import java.sql.SQLException;

import org.veupathdb.service.userds.util.DbMan;

public class InsertFileQuery
{
  public static void run(int dbId, String name, long size) throws SQLException {
    try (
      var cn = DbMan.getImportDb().getConnection();
      var ps = cn.prepareStatement(Query.insertFile())
    ) {
      ps.setInt(1, dbId);
      ps.setString(2, name);
      ps.setLong(3, size);
      ps.execute();
    }
  }
}
