package org.veupathdb.service.userds.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum JobStatus {
  @JsonProperty("receiving-from-client")
  RECEIVINGFROMCLIENT("receiving-from-client"),

  @JsonProperty("sending-to-handler")
  SENDINGTOHANDLER("sending-to-handler"),

  @JsonProperty("handler-unpacking")
  HANDLERUNPACKING("handler-unpacking"),

  @JsonProperty("handler-processing")
  HANDLERPROCESSING("handler-processing"),

  @JsonProperty("handler-packing")
  HANDLERPACKING("handler-packing"),

  @JsonProperty("recieving-from-handler")
  RECIEVINGFROMHANDLER("recieving-from-handler"),

  @JsonProperty("sending-to-datastore")
  SENDINGTODATASTORE("sending-to-datastore"),

  @JsonProperty("success")
  SUCCESS("success"),

  @JsonProperty("failed")
  FAILED("failed"),

  @JsonProperty("errored")
  ERRORED("errored");

  private String name;

  JobStatus(String name) {
    this.name = name;
  }
}
