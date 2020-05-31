package org.veupathdb.service.userds.repo;

import java.sql.SQLException;

import org.veupathdb.service.userds.model.ProjectCache;
import org.veupathdb.service.userds.model.StatusCache;
import org.veupathdb.service.userds.util.DbMan;

/**
 * Select Project Type Values
 *
 * Populates the cache of possible project types from the database.
 */
public class SelectProjectQuery
{
  public static void run() throws SQLException {
    var out = ProjectCache.getInstance();

    try (
      var cn = DbMan.getImportDb().getConnection();
      var st = cn.createStatement();
      var rs = st.executeQuery(Query.selectProjects())
    ) {
      while (rs.next())
        out.put(rs.getString(2), rs.getShort(1));
    }
  }
}
