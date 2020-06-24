package org.veupathdb.service.userds.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.veupathdb.service.userds.model.JobRow;
import org.veupathdb.service.userds.util.DbMan;

/**
 * Select User Jobs
 * <p>
 * Query that returns a list of JobRow instances for the given user ID.  If the
 * user has no jobs, the returned list will be empty.
 */
public class SelectJobsQuery extends SelectJobBase
{
  public static final int
    DEFAULT_LIMIT  = 100,
    DEFAULT_OFFSET = 0;

  public static List < JobRow > run(long userId, int limit, int page)
  throws Exception {
    if (limit == 0)
      return Collections.emptyList();

    if (limit < 0)
      throw new IllegalArgumentException("Record limit cannot be less than 0");

    if (page < 0)
      throw new IllegalArgumentException("Page number cannot be less than 0");

    var out = new ArrayList < JobRow >(10);

    try (
      var con = DbMan.getImportDb().getConnection();
      var ps = prepareStatement(con, userId, limit, page);
      var rs = ps.executeQuery()
    ) {
      while (rs.next())
        out.add(rsToJobRow(rs));
    }

    return out;
  }

  private static PreparedStatement prepareStatement(
    Connection con,
    long userId,
    int limit,
    int page
  )
  throws SQLException {
    final var out = con.prepareStatement(Query.selectJobList());
    out.setLong(1, userId);
    out.setInt(2, limit);
    out.setInt(3, page * limit);
    return out;
  }
}
