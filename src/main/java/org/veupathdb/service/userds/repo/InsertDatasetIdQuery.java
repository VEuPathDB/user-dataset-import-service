package org.veupathdb.service.userds.repo;

import org.veupathdb.service.userds.util.DbMan;

public class InsertDatasetIdQuery
{
  public static void run(int dbId, int irodsId) throws Exception {
    try (
      var cn = DbMan.getImportDb().getConnection();
      var ps = cn.prepareStatement(Query.insertIrodsId())
    ) {
      ps.setInt(1, dbId);
      ps.setInt(2, irodsId);
      ps.execute();
    }
  }
}
