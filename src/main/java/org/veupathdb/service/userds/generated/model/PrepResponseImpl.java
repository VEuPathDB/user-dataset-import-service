package org.veupathdb.service.userds.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "status",
    "token"
})
public class PrepResponseImpl implements PrepResponse {
  @JsonProperty("status")
  private PrepResponse.StatusType status;

  @JsonProperty("token")
  private String token;

  @JsonProperty("status")
  public PrepResponse.StatusType getStatus() {
    return this.status;
  }

  @JsonProperty("status")
  public void setStatus(PrepResponse.StatusType status) {
    this.status = status;
  }

  @JsonProperty("token")
  public String getToken() {
    return this.token;
  }

  @JsonProperty("token")
  public void setToken(String token) {
    this.token = token;
  }
}
