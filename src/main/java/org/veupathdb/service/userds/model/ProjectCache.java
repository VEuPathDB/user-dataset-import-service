package org.veupathdb.service.userds.model;

import java.util.HashMap;
import java.util.Map;

public class ProjectCache extends HashMap<String, Short> {
  private static ProjectCache instance;

  private Map<Short, String> inverse;
  private ProjectCache() {
    super(16);
  }

  public String getKey(Short value) {
    populateInverse();
    return inverse.get(value);
  }

  public static ProjectCache getInstance() {
    if (instance == null)
      instance = new ProjectCache();

    return instance;
  }

  private void populateInverse() {
    if (inverse != null)
      return;

    inverse = new HashMap <>(16);
    forEach((k, v) -> inverse.put(v, k));
  }
}
