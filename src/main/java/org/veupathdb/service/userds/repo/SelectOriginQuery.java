package org.veupathdb.service.userds.repo;

import java.sql.SQLException;

import org.veupathdb.service.userds.model.handler.DatasetOrigin;
import org.veupathdb.service.userds.model.handler.DatasetOriginCache;
import org.veupathdb.service.userds.util.DbMan;

/**
 * Select Status Type Values
 *
 * Populates the cache of possible status type values based on the result of the
 * query and the built-in enum.
 *
 * If the hardcoded enum has a different number or set of values than the
 * database table, a RuntimeException will be thrown.
 */
public class SelectOriginQuery
{
  private static final String
    errOutOfSyncKey = "database origins are out of sync with origin enum on" +
    " key %s",
    errOutOfSyncLen = "database origins are out of sync, DB has %d enum has %d";

  public static void run() throws SQLException {
    var out = DatasetOriginCache.getInstance();

    try (
      var cn = DbMan.getImportDb().getConnection();
      var st = cn.createStatement();
      var rs = st.executeQuery(SQL.Select.Origin.All)
    ) {
      var i = 0;

      while (rs.next()) {
        var key  = rs.getString(2);
        var name = DatasetOrigin.fromString(key)
          .orElseThrow(() -> new RuntimeException(String.format(errOutOfSyncKey,
            key)));
        out.put(rs.getShort(1), name);
        i++;
      }

      if (i != DatasetOrigin.values().length)
        throw new RuntimeException(String.format(errOutOfSyncLen, i,
          DatasetOrigin.values().length));
    }
  }
}
