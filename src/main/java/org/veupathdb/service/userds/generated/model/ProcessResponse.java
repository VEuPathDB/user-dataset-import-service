package org.veupathdb.service.userds.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = ProcessResponseImpl.class
)
public interface ProcessResponse {
  @JsonProperty("id")
  String getId();

  @JsonProperty("id")
  void setId(String id);

  @JsonProperty("status")
  StatusType getStatus();

  @JsonProperty("status")
  void setStatus(StatusType status);

  enum StatusType {
    @JsonProperty("ok")
    OK("ok");

    private String name;

    StatusType(String name) {
      this.name = name;
    }
  }
}
