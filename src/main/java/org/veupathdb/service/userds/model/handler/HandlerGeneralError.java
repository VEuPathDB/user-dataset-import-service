package org.veupathdb.service.userds.model.handler;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HandlerGeneralError
{
  private final String status;
  private final String message;

  @JsonCreator
  public HandlerGeneralError(
    @JsonProperty("status") final String status,
    @JsonProperty("message") final String message
  ) {
    this.status = status;
    this.message = message;
  }

  public String getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }
}
