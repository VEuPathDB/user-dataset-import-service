package org.veupathdb.service.userds.repo;

import java.util.function.Supplier;

import io.vulpine.lib.sql.load.SqlLoader;

public class Query
{
  private static final SqlLoader loader = new SqlLoader();
  private static final String err = "failed to load query sql.%s.%s";

  // Prepopulate cache and check for missing queries.
  static {
    deleteFile();

    insertFile();
    insertJob();
    insertMessage();

    selectJobList();
    selectSingleJob();
    selectProjects();
    selectStatuses();

    updateJobStatus();
  }

  public static String deleteFile() {
    return loader.delete("file").orElseThrow(throwable("delete", "file"));
  }


  public static String insertFile() {
    return loader.insert("file")
      .orElseThrow(throwable("insert", "file"));
  }

  public static String insertJob() {
    return loader.insert("job")
      .orElseThrow(throwable("insert", "job"));
  }

  public static String insertMessage() {
    return loader.insert("message")
      .orElseThrow(throwable("insert", "message"));
  }


  public static String selectJobList() {
    return loader.select("jobs-by-user")
      .orElseThrow(throwable("select", "jobs-by-user"));
  }

  public static String selectSingleJob() {
    return loader.select("job-by-id")
      .orElseThrow(throwable("select", "job-by-id"));
  }

  public static String selectStatuses() {
    return loader.select("statuses")
      .orElseThrow(throwable("select", "statuses"));
  }

  public static String selectProjects() {
    return loader.select("projects")
      .orElseThrow(throwable("select", "projects"));
  }


  public static String updateJobStatus() {
    return loader.udpate("job-status")
      .orElseThrow(throwable("update", "job-status"));
  }

  private static Supplier<RuntimeException> throwable(String mode, String sql) {
    return () -> new RuntimeException(String.format(err, mode, sql));
  }
}
