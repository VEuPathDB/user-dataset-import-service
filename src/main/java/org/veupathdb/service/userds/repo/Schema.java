package org.veupathdb.service.userds.repo;

public interface Schema
{
  interface Column
  {
    String
      DB_ID       = "db_id",
      DESCRIPTION = "description",
      FILE_NAME   = "file",
      FILE_SIZE   = "total_size",
      FINISHED    = "finished",
      JOB_ID      = "job_id",
      MESSAGE     = "message",
      NAME        = "name",
      PROJECTS    = "projects",
      STARTED     = "started",
      STATUS      = "status",
      SUMMARY     = "summary",
      USER_ID     = "user_id";
  }
}
