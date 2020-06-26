package org.veupathdb.service.userds.repo.meta;

import org.veupathdb.service.userds.repo.SQL;
import org.veupathdb.service.userds.util.DbMan;

/**
 * SQL statement to retrieve the current version number stored in the database
 * for the service.
 *
 * This version number is used to handle auto-migrations.
 */
public class SelectVersion
{
  public static String run() throws Exception {
    try (
      var con = DbMan.getImportDb().getConnection();
      var stm = con.createStatement();
      var rs  = stm.executeQuery(SQL.Select.Meta.Version)
    ) {
      rs.next();
      return rs.getString(1);
    }
  }
}
