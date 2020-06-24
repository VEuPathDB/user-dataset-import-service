package org.veupathdb.service.userds.model.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DatasetOriginCache
{
  private static DatasetOriginCache instance;

  private final Map < Short, DatasetOrigin > byId;
  private final Map < DatasetOrigin, Short > byName;

  public DatasetOriginCache() {
    byId = new HashMap <>(2);
    byName = new HashMap <>(2);
  }

  public synchronized DatasetOriginCache put(short id, DatasetOrigin val) {
    byId.put(id, val);
    byName.put(val, id);
    return this;
  }

  public synchronized Optional < DatasetOrigin > get(short id) {
    return Optional.ofNullable(byId.get(id));
  }

  public synchronized Optional < Short > get(DatasetOrigin val) {
    return Optional.ofNullable(byName.get(val));
  }

  public static DatasetOriginCache getInstance() {
    if (instance == null)
      instance = new DatasetOriginCache();

    return instance;
  }
}
