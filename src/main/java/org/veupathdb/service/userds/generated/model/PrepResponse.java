package org.veupathdb.service.userds.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = PrepResponseImpl.class
)
public interface PrepResponse {
  @JsonProperty("status")
  String getStatus();

  @JsonProperty("jobId")
  String getJobId();

  @JsonProperty("jobId")
  PrepResponse setJobId(String jobId);
}
