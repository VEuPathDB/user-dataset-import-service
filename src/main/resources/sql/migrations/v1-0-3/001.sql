CREATE TABLE IF NOT EXISTS ds_meta.kv_store
(
  key VARCHAR(32) NOT NULL PRIMARY KEY,
  value VARCHAR(256)
);