package org.veupathdb.service.userds.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PrepResponseImpl implements PrepResponse {
  @JsonProperty("jobId")
  private String jobId;

  @JsonProperty("status")
  public String getStatus() {
    return "ok";
  }

  @JsonProperty("jobId")
  public String getJobId() {
    return this.jobId;
  }

  @JsonProperty("jobId")
  public PrepResponse setJobId(String jobId) {
    this.jobId = jobId;
    return this;
  }
}
