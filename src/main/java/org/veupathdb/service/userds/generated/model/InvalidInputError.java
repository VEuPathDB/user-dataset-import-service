package org.veupathdb.service.userds.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import java.util.Map;

@JsonTypeName("invalid-input")
@JsonDeserialize(
  as = InvalidInputErrorImpl.class
)
public interface InvalidInputError extends ErrorResponse
{
  String _DISCRIMINATOR_TYPE_NAME = "invalid-input";

  @JsonProperty("status")
  String getStatus();

  @JsonProperty("message")
  String getMessage();

  @JsonProperty("message")
  void setMessage(String message);

  @JsonProperty("errors")
  ErrorsType getErrors();

  @JsonProperty("errors")
  void setErrors(ErrorsType errors);

  @JsonDeserialize(
    as = InvalidInputErrorImpl.ErrorsTypeImpl.class
  )
  interface ErrorsType
  {
    @JsonProperty("general")
    List < String > getGeneral();

    @JsonProperty("general")
    void setGeneral(List < String > general);

    @JsonProperty("byKey")
    ByKeyType getByKey();

    @JsonProperty("byKey")
    void setByKey(ByKeyType byKey);

    @JsonDeserialize(
      as = InvalidInputErrorImpl.ErrorsTypeImpl.ByKeyTypeImpl.class
    )
    interface ByKeyType
    {
      @JsonAnyGetter
      Map < String, List < String > > getAdditionalProperties();

      @JsonAnySetter
      void setAdditionalProperties(String key, List < String > value);
    }
  }
}
