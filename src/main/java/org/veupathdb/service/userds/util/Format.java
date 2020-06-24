package org.veupathdb.service.userds.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.veupathdb.service.userds.model.handler.DatasetOrigin;
import org.veupathdb.service.userds.util.json.DatasetOriginDeserializer;
import org.veupathdb.service.userds.util.json.DatasetOriginSerializer;

public class Format
{
  public static final ObjectMapper Json = new ObjectMapper()
    .registerModule(new SimpleModule()
      .addDeserializer(DatasetOrigin.class, new DatasetOriginDeserializer())
      .addSerializer(DatasetOrigin.class, new DatasetOriginSerializer()));
}
