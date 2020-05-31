package org.veupathdb.service.userds.model.handler;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HandlerValidationResult
{
  private final List < String > general;

  private final Map < String, List < String > > byKey;

  @JsonCreator
  public HandlerValidationResult(
    @JsonProperty("general") final List < String > general,
    @JsonProperty("byKey") final Map < String, List < String > > byKey
  ) {
    this.general = Collections.unmodifiableList(general);
    this.byKey = Collections.unmodifiableMap(byKey);
  }

  public List < String > getGeneral() {
    return general;
  }

  public Map < String, List < String > > getByKey() {
    return byKey;
  }
}
