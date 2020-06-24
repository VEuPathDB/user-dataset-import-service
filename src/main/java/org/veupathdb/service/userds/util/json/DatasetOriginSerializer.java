package org.veupathdb.service.userds.util.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.veupathdb.service.userds.model.handler.DatasetOrigin;

public class DatasetOriginSerializer extends StdSerializer< DatasetOrigin >
{
  public DatasetOriginSerializer() {
    super(DatasetOrigin.class);
  }

  @Override
  public void serialize(
    final DatasetOrigin value,
    final JsonGenerator gen,
    final SerializerProvider provider
  ) throws IOException {
    gen.writeString(value.getValue());
  }
}
