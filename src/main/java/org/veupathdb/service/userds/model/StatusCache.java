package org.veupathdb.service.userds.model;

import java.util.HashMap;

public final class StatusCache extends HashMap<JobStatus, Short> {
  private static StatusCache instance;

  private StatusCache() {
    super(10);
  }

  public static StatusCache getInstance() {
    if (instance == null)
      instance = new StatusCache();

    return instance;
  }
}
