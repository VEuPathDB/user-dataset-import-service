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
  static final String errOutOfSync = "database statuses are out of sync with " +
    "status enum";

  public static void run() throws SQLException {
    var out = StatusCache.getInstance();

    try (
      var cn = DbMan.getImportDb().getConnection();
      var st = cn.createStatement();
      var rs = st.executeQuery(Query.selectStatuses())
    ) {
      var i = 0;

      while (rs.next()) {
        var name = JobStatus.fromString(rs.getString(2))
          .orElseThrow(() -> new RuntimeException(errOutOfSync));
        out.put(name, rs.getShort(1));
        i++;
      }

      if (i != JobStatus.values().length)
        throw new RuntimeException(errOutOfSync);
    }
  }
}
