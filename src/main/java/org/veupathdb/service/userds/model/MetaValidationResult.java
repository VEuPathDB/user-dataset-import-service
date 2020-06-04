package org.veupathdb.service.userds.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetaValidationResult
{
  private final List < String > general;

  private final Map < String, List < String > > byKey;

  public MetaValidationResult() {
    general = new ArrayList <>();
    byKey = new HashMap <>();
  }

  public List < String > getGeneral() {
    return general;
  }

  public Map < String, List < String > > getByKey() {
    return byKey;
  }

  public boolean containsErrors() {
    return !(general.isEmpty() && byKey.isEmpty());
  }
}
