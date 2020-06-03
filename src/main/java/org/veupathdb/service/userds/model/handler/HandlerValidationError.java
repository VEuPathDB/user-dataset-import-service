package org.veupathdb.service.userds.model.handler;

import com.fasterxml.jackson.annotation.JsonSetter;

public class HandlerValidationError
{
  public static final String
    KEY_ERRORS = "reasons",
    KEY_STATUS = "status";

  private String status;

  private HandlerValidationResult errors;

  @JsonSetter(KEY_STATUS)
  public void setStatus(String status) {
    this.status = status;
  }

  @JsonSetter(KEY_ERRORS)
  public void setErrors(HandlerValidationResult errors) {
    this.errors = errors;
  }

  public String getStatus() {
    return status;
  }

  public HandlerValidationResult getErrors() {
    return errors;
  }
}

