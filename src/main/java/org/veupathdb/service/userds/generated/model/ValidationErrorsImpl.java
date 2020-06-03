package org.veupathdb.service.userds.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("errors")
public class ValidationErrorsImpl implements ValidationErrors {

  @JsonProperty("errors")
  private final ValidationErrors.ErrorsType errors;

  @JsonIgnore
  private final Map<String, Object> additionalProperties;

  public ValidationErrorsImpl() {
    errors = new ErrorsTypeImpl();
    additionalProperties = new ExcludingMap();
  }

  @JsonProperty("errors")
  public ValidationErrors.ErrorsType getErrors() {
    return this.errors;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperties(String key, Object value) {
    this.additionalProperties.put(key, value);
  }

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonPropertyOrder({"general", "byKey"})
  public static class ErrorsTypeImpl implements ValidationErrors.ErrorsType {
    @JsonProperty("general")
    private List<String> general;

    @JsonProperty("byKey")
    private ValidationErrors.ErrorsType.ByKeyType byKey;

    public ErrorsTypeImpl() {
      this.general = new ArrayList <>();
      this.byKey = new ByKeyTypeImpl();
    }

    @JsonProperty("general")
    public List<String> getGeneral() {
      return this.general;
    }

    @JsonProperty("general")
    public void setGeneral(List<String> general) {
      this.general = general;
    }

    @JsonProperty("byKey")
    public ValidationErrors.ErrorsType.ByKeyType getByKey() {
      return this.byKey;
    }

    @JsonProperty("byKey")
    public void setByKey(ValidationErrors.ErrorsType.ByKeyType byKey) {
      this.byKey = byKey;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder
    public static class ByKeyTypeImpl implements ValidationErrors.ErrorsType.ByKeyType {
      @JsonIgnore
      private Map<String, Object> additionalProperties = new ExcludingMap();

      @JsonAnyGetter
      public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
      }

      @JsonAnySetter
      public void setAdditionalProperties(String key, Object value) {
        this.additionalProperties.put(key, value);
      }
    }
  }
}
