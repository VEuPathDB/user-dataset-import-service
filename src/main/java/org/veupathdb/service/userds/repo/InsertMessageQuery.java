package org.veupathdb.service.userds.repo;

import java.sql.SQLException;

import com.fasterxml.jackson.databind.JsonNode;
import org.veupathdb.service.userds.util.DbMan;

public class InsertMessageQuery
{
  public static void run(int dbId, JsonNode msg) throws SQLException {
    run(dbId, msg.toString());
  }

  public static void run(int dbId, String msg) throws SQLException {
    try (
      var cn = DbMan.getImportDb().getConnection();
      var ps = cn.prepareStatement(Query.insertMessage())
    ) {
      ps.setInt(1, dbId);
      ps.setString(2, msg);
      ps.execute();
    }
  }
}
