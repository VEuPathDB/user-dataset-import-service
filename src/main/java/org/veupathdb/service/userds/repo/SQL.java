package org.veupathdb.service.userds.repo;

import java.util.function.Supplier;

import io.vulpine.lib.sql.load.SqlLoader;

public interface SQL
{
  SqlLoader SQL_LOADER   = new SqlLoader();
  String    ERR_SQL_LOAD = "failed to load query sql.%s.%s";

  interface Select
  {
    interface Job
    {
      String
        ByJobID  = select("job-by-id"),
        ByUserID = select("jobs-by-user");
    }

    interface Meta
    {
      String
        MetaSchemaExists = select("store-exists"),
        Version          = select("service-version");
    }

    interface Origin
    {
      String All = select("origins");
    }

    interface Project
    {
      String All = select("projects");
    }

    interface Status
    {
      String All = select("statuses");
    }


    private static String select(String path) {
      return SQL_LOADER.select(path)
        .orElseThrow(throwable("select", path));
    }
  }

  interface Delete
  {
    String
      Job = delete("job-by-id");

    private static String delete(String path) {
      return SQL_LOADER.delete(path)
        .orElseThrow(throwable("delete", path));
    }
  }

  interface Insert
  {
    String
      IrodsID = insert("irods-id"),
      Job     = insert("job"),
      Message = insert("message");

    private static String insert(String path) {
      return SQL_LOADER.insert(path)
        .orElseThrow(throwable("insert", path));
    }
  }

  interface Update
  {
    interface Job
    {
      String
        Status    = update("job-status"),
        Completed = update("job-completed");
    }

    private static String update(String path) {
      return SQL_LOADER.udpate(path)
        .orElseThrow(throwable("update", path));
    }
  }

  private static Supplier < RuntimeException > throwable(
    String mode,
    String sql
  ) {
    return () -> new RuntimeException(String.format(ERR_SQL_LOAD, mode, sql));
  }
}
