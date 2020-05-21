package org.veupathdb.service.userds.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = PrepResponseImpl.class
)
public interface PrepResponse {
  @JsonProperty("status")
  StatusType getStatus();

  @JsonProperty("status")
  void setStatus(StatusType status);

  @JsonProperty("token")
  String getToken();

  @JsonProperty("token")
  void setToken(String token);

  enum StatusType {
    @JsonProperty("ok")
    OK("ok");

    private String name;

    StatusType(String name) {
      this.name = name;
    }
  }
}
