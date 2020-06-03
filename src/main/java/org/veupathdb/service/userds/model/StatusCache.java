package org.veupathdb.service.userds.model;

import java.util.HashMap;
import java.util.Map;

public final class StatusCache extends HashMap < JobStatus, Short >
{
  private static StatusCache instance;

  private Map < Short, JobStatus > inverse;

  private StatusCache() {
    super(10);
  }

  public JobStatus getKey(Short value) {
    populateInverse();
    return inverse.get(value);
  }

  public Map < Short, JobStatus > getInverse() {
    return inverse;
  }

  private void populateInverse() {
    if (inverse != null)
      return;

    inverse = new HashMap <>(16);
    forEach((k, v) -> inverse.put(v, k));
  }

  public static StatusCache getInstance() {
    if (instance == null)
      instance = new StatusCache();

    return instance;
  }
}
