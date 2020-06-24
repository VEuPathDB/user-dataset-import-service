package org.veupathdb.service.userds.model.handler;

import java.util.Optional;

public enum DatasetOrigin
{
  DIRECT_UPLOAD("direct-upload"),
  GALAXY("galaxy");

  private final String value;

  DatasetOrigin(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static Optional < DatasetOrigin > fromString(String value) {
    for (var val : values())
      if (val.value.equals(value))
        return Optional.of(val);

    return Optional.empty();
  }
}
