package org.veupathdb.service.userds.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "status"
})
public class ProcessResponseImpl implements ProcessResponse {
  @JsonProperty("id")
  private String id;

  @JsonProperty("status")
  private ProcessResponse.StatusType status;

  @JsonProperty("id")
  public String getId() {
    return this.id;
  }

  @JsonProperty("id")
  public void setId(String id) {
    this.id = id;
  }

  @JsonProperty("status")
  public ProcessResponse.StatusType getStatus() {
    return this.status;
  }

  @JsonProperty("status")
  public void setStatus(ProcessResponse.StatusType status) {
    this.status = status;
  }
}
