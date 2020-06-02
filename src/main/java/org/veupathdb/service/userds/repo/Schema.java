package org.veupathdb.service.userds.repo;

public interface Schema
{
  interface Column
  {
    String
      DB_ID       = "db_id",
      DESCRIPTION = "description",
      FINISHED    = "finished",
      JOB_ID      = "job_id",
      MESSAGE     = "message",
      NAME        = "name",
      PROJECTS    = "projects",
      STARTED     = "started",
      STATUS      = "status_id",
      SUMMARY     = "summary",
      USER_ID     = "user_id";
  }
}
