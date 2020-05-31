package org.veupathdb.service.userds.model;

import java.util.Arrays;
import java.util.Optional;

public enum JobStatus
{
  AWAITING_UPLOAD("awaiting-upload"),
  SENDING_TO_HANDLER("sending-to-handler"),
  HANDLER_UNPACKING("handler-unpacking"),
  HANDLER_PROCESSING("handler-processing"),
  HANDLER_PACKING("handler-packing"),
  RECEIVING_FROM_HANDLER("receiving-from-handler"),
  SENDING_TO_DATASTORE("sending-to-datastore"),
  SUCCESS("success"),
  REJECTED("rejected"),
  ERRORED("errored");

  private final String name;

  JobStatus(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public static Optional <JobStatus> fromString(String name) {
    return Arrays.stream(values())
      .filter(v -> v.name.equals(name))
      .findAny();
  }
}
