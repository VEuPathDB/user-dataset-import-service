package org.veupathdb.service.userds.model.handler;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HandlerGeneralError
{
  private int code;
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

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }
}
