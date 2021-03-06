/*----------------------------------------------------------------------------*\
|                                                                              |
|  Service Metadata                                                            |
|                                                                              |
\*----------------------------------------------------------------------------*/

CREATE SCHEMA ds_meta;

CREATE TABLE IF NOT EXISTS ds_meta.kv_store
(
  key VARCHAR(32) NOT NULL PRIMARY KEY,
  value VARCHAR(256)
);

INSERT INTO
  ds_meta.kv_store (key, value)
VALUES
  ('version', 'v1.0.3')
;

/*----------------------------------------------------------------------------*\
|                                                                              |
|  Static Components                                                           |
|                                                                              |
\*----------------------------------------------------------------------------*/

CREATE SCHEMA ds_types;

/*--------------------------------------------------------*\
|  Project Mapping                                         |
\*--------------------------------------------------------*/

CREATE TABLE IF NOT EXISTS ds_types.project
(
  project_id SMALLSERIAL PRIMARY KEY,
  name       VARCHAR(24) NOT NULL UNIQUE
);

INSERT INTO
  ds_types.project (name)
VALUES
  ('AmoebaDB')
, ('ClinEpiDB')
, ('CryptoDB')
, ('FungiDB')
, ('GiardiaDB')
, ('HostDB')
, ('MicrobiomeDB')
, ('MicrosporidiaDB')
, ('OrthoMCL')
, ('PiroplasmaDB')
, ('PlasmoDB')
, ('SchistoDB')
, ('ToxoDB')
, ('TrichDB')
, ('TritrypDB')
, ('VectorBase')
;

/*--------------------------------------------------------*\
|  Status Mapping                                          |
\*--------------------------------------------------------*/

CREATE TABLE IF NOT EXISTS ds_types.status
(
  status_id SMALLSERIAL PRIMARY KEY,
  name      VARCHAR(24) NOT NULL UNIQUE
);

INSERT INTO
  ds_types.status (name)
VALUES
  ('awaiting-upload')
, ('sending-to-handler')
, ('handler-unpacking')
, ('handler-processing')
, ('handler-packing')
, ('receiving-from-handler')
, ('sending-to-datastore')
, ('datastore-unpacking')
, ('success')
, ('rejected')
, ('errored')
;

/*--------------------------------------------------------*\
|  Origin Mapping                                          |
\*--------------------------------------------------------*/

CREATE TABLE IF NOT EXISTS ds_types.origin
(
  origin_id SMALLSERIAL PRIMARY KEY,
  name      VARCHAR(24) NOT NULL UNIQUE
);

INSERT INTO
  ds_types.origin (name)
VALUES
  ('galaxy')
, ('direct-upload')
;


/*----------------------------------------------------------------------------*\
|                                                                              |
|  User Data                                                                   |
|                                                                              |
\*----------------------------------------------------------------------------*/

CREATE SCHEMA IF NOT EXISTS ds_jobs;

/*--------------------------------------------------------*\
|  Import Jobs                                             |
\*--------------------------------------------------------*/

CREATE TABLE IF NOT EXISTS ds_jobs.jobs
(
  db_id       SERIAL PRIMARY KEY
, job_id      VARCHAR(22)  NOT NULL UNIQUE
, user_id     BIGINT       NOT NULL
, status_id   SMALLINT     NOT NULL
    REFERENCES ds_types.status (status_id) ON DELETE RESTRICT
, name        VARCHAR(256) NOT NULL
, description VARCHAR
, summary     VARCHAR
, origin_id   SMALLINT     NOT NULL
    REFERENCES ds_types.origin (origin_id) ON DELETE RESTRICT
, type        VARCHAR(16)  NOT NULL
, started     TIMESTAMPTZ  NOT NULL DEFAULT now()
, finished    TIMESTAMPTZ
);

CREATE INDEX IF NOT EXISTS jobs_user_id ON ds_jobs.jobs (user_id);

/*--------------------------------------------------------*\
|  Job Project Attachments                                 |
\*--------------------------------------------------------*/

CREATE TABLE IF NOT EXISTS ds_jobs.job_projects
(
  db_id      INT NOT NULL REFERENCES ds_jobs.jobs (db_id) ON DELETE CASCADE
, project_id INT NOT NULL
    REFERENCES ds_types.project (project_id) ON DELETE CASCADE
);

/*--------------------------------------------------------*\
|  Job Error Messages                                      |
\*--------------------------------------------------------*/

CREATE TABLE IF NOT EXISTS ds_jobs.job_messages
(
  db_id   INT   NOT NULL REFERENCES ds_jobs.jobs (db_id) ON DELETE CASCADE
, message JSONB NOT NULL
);

/*--------------------------------------------------------*\
|  Job iRODS Attachments                                   |
\*--------------------------------------------------------*/

CREATE TABLE IF NOT EXISTS ds_jobs.job_irods_id
(
  db_id INT NOT NULL UNIQUE REFERENCES ds_jobs.jobs (db_id) ON DELETE CASCADE
, irods_id INT NOT NULL UNIQUE
);
