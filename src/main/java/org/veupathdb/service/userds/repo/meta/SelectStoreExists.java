package org.veupathdb.service.userds.repo.meta;

import org.veupathdb.service.userds.repo.SQL;
import org.veupathdb.service.userds.util.DbMan;

/**
 * SQL <code>SELECT</code> check to see if the meta schema exists.
 */
public class SelectStoreExists
{
  public static boolean run() throws Exception {
    try (
      var con = DbMan.getImportDb().getConnection();
      var stm = con.createStatement();
      var rs  = stm.executeQuery(SQL.Select.Meta.MetaSchemaExists)
    ) {
      return rs.next();
    }
  }
}
