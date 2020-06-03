package org.veupathdb.service.userds.model.handler;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonSetter;

public class HandlerValidationResult
{
  public static final String
    KEY_GENERAL = "general",
    KEY_BY_KEY  = "byKey";

  private List < String > general;

  private Map < String, List < String > > byKey;

  @JsonSetter(KEY_GENERAL)
  public void setGeneral(List < String > general) {
    this.general = general;
  }

  @JsonSetter(KEY_BY_KEY)
  public void setByKey(Map < String, List < String > > byKey) {
    this.byKey = byKey;
  }

  public List < String > getGeneral() {
    return general;
  }

  public Map < String, List < String > > getByKey() {
    return byKey;
  }
}
