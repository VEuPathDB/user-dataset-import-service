package org.veupathdb.service.userds.model.handler;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HandlerValidationError
{
  public static final String
    KEY_ERRORS = "errors",
    KEY_STATUS = "status";

  private final String status;

  private final HandlerValidationResult errors;

  @JsonCreator
  public HandlerValidationError(
    @JsonProperty(KEY_STATUS) String status,
    @JsonProperty(KEY_ERRORS) HandlerValidationResult errors
  ) {
    this.status = status;
    this.errors = errors;
  }

  public String getStatus() {
    return status;
  }

  public HandlerValidationResult getErrors() {
    return errors;
  }
}

