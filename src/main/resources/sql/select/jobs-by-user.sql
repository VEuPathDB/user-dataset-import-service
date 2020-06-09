SELECT
  *,
  (
    SELECT array_agg(project_id)
    FROM ds_jobs.job_projects
    WHERE db_id = jobs.db_id
  ) AS projects
FROM
  ds_jobs.jobs
  LEFT JOIN ds_jobs.job_messages
    USING (db_id)
  LEFT JOIN ds_jobs.job_irods_id
    USING (db_id)
WHERE
  user_id = ?
ORDER BY
  started DESC
;
