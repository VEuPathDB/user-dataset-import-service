package org.veupathdb.service.userds.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonTypeName("not-found")
@JsonDeserialize(
    as = NotFoundErrorImpl.class
)
public interface NotFoundError extends ErrorResponse {
  String _DISCRIMINATOR_TYPE_NAME = "not-found";

  @JsonProperty("status")
  String getStatus();

  @JsonProperty("message")
  String getMessage();

  @JsonProperty("message")
  void setMessage(String message);
}
