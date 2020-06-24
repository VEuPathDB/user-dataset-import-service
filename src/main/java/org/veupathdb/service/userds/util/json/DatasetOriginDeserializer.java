package org.veupathdb.service.userds.util.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import org.veupathdb.service.userds.model.handler.DatasetOrigin;
import org.veupathdb.service.userds.util.Format;

public class DatasetOriginDeserializer extends StdDeserializer< DatasetOrigin >
{
  private static final String error = "unrecognized dataset origin value \"%s\"";

  public DatasetOriginDeserializer() {
    super(DatasetOrigin.class);
  }

  @Override
  public DatasetOrigin deserialize(
    final JsonParser p,
    final DeserializationContext ctxt
  ) throws IOException {
    final var tmp = p.readValueAs(String.class);
    return DatasetOrigin.fromString(tmp)
      .orElseThrow(() -> ValueInstantiationException.from(
        p, tmp, Format.Json.constructType(DatasetOrigin.class)));
  }
}
