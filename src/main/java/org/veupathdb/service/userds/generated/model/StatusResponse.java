package org.veupathdb.service.userds.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@JsonDeserialize(
    as = StatusResponseImpl.class
)
public interface StatusResponse {
  @JsonProperty("id")
  String getId();

  @JsonProperty("id")
  StatusResponse setId(String id);

  @JsonProperty("datasetName")
  String getDatasetName();

  @JsonProperty("datasetName")
  StatusResponse setDatasetName(String datasetName);

  @JsonProperty("description")
  String getDescription();

  @JsonProperty("description")
  StatusResponse setDescription(String description);

  @JsonProperty("summary")
  String getSummary();

  @JsonProperty("summary")
  StatusResponse setSummary(String summary);

  @JsonProperty("stepPercent")
  int getStepPercent();

  @JsonProperty("stepPercent")
  StatusResponse setStepPercent(int stepPercent);

  @JsonProperty("projects")
  List<String> getProjects();

  @JsonProperty("projects")
  StatusResponse setProjects(List<String> projects);

  @JsonProperty("status")
  String getStatus();

  @JsonProperty("status")
  StatusResponse setStatus(String status);

  @JsonProperty("statusDetails")
  StatusDetailsType getStatusDetails();

  @JsonProperty("statusDetails")
  StatusResponse setStatusDetails(StatusDetailsType statusDetails);

  @JsonProperty("started")
  Date getStarted();

  @JsonProperty("started")
  StatusResponse setStarted(Date started);

  @JsonDeserialize(
      using = StatusDetailsType.StatusDetailsDeserializer.class
  )
  @JsonSerialize(
      using = StatusDetailsType.Serializer.class
  )
  interface StatusDetailsType {
    ValidationErrors getValidationErrors();

    boolean isValidationErrors();

    JobError getJobError();

    boolean isJobError();

    class Serializer extends StdSerializer<StatusDetailsType> {
      public Serializer() {
        super(StatusDetailsType.class);}

      public void serialize(StatusDetailsType object, JsonGenerator jsonGenerator,
          SerializerProvider jsonSerializerProvider) throws IOException, JsonProcessingException {
        if ( object.isValidationErrors()) {
          jsonGenerator.writeObject(object.getValidationErrors());
          return;
        }
        if ( object.isJobError()) {
          jsonGenerator.writeObject(object.getJobError());
          return;
        }
        throw new IOException("Can't figure out type of object" + object);
      }
    }

    class StatusDetailsDeserializer extends StdDeserializer<StatusDetailsType> {
      public StatusDetailsDeserializer() {
        super(StatusDetailsType.class);}

      private boolean looksLikeValidationErrors(Map<String, Object> map) {
        return map.keySet().containsAll(Arrays.asList("errors"));
      }

      private boolean looksLikeJobError(Map<String, Object> map) {
        return map.keySet().containsAll(Arrays.asList("message"));
      }

      public StatusDetailsType deserialize(JsonParser jsonParser,
          DeserializationContext jsonContext) throws IOException, JsonProcessingException {
        ObjectMapper mapper  = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(jsonParser, Map.class);
        if ( looksLikeValidationErrors(map) ) return new StatusResponseImpl.StatusDetailsTypeImpl(mapper.convertValue(map, ValidationErrorsImpl.class));
        if ( looksLikeJobError(map) ) return new StatusResponseImpl.StatusDetailsTypeImpl(mapper.convertValue(map, JobErrorImpl.class));
        throw new IOException("Can't figure out type of object" + map);
      }
    }
  }
}
