package org.veupathdb.service.userds.repo;

import java.sql.SQLException;

import org.veupathdb.service.userds.model.JobStatus;
import org.veupathdb.service.userds.model.StatusCache;
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
public class SelectStatusQuery
{
  private static final String
    errOutOfSyncKey = "database statuses are out of sync with status enum on" +
    " key %s",
    errOutOfSyncLen = "database statuses are out of sync, DB has %d enum has" +
      " %d";

  public static void run() throws SQLException {
    var out = StatusCache.getInstance();

    try (
      var cn = DbMan.getImportDb().getConnection();
      var st = cn.createStatement();
      var rs = st.executeQuery(SQL.Select.Status.All)
    ) {
      var i = 0;

      while (rs.next()) {
        var key  = rs.getString(2);
        var name = JobStatus.fromString(key)
          .orElseThrow(() -> new RuntimeException(String.format(errOutOfSyncKey,
            key)));
        out.put(name, rs.getShort(1));
        i++;
      }

      if (i != JobStatus.values().length)
        throw new RuntimeException(String.format(errOutOfSyncLen, i,
          JobStatus.values().length));
    }
  }
}
