package org.veupathdb.service.userds.util;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.net.URL;

import org.veupathdb.lib.container.jaxrs.health.ServiceDependency;

import static org.apache.logging.log4j.LogManager.getLogger;

public class HandlerDependency extends ServiceDependency {

  public HandlerDependency(String name, String url, int port) {
    super(name, url, port);
  }

  @Override
  protected TestResult serviceTest() {
    try {
      final var node = Format.Json.reader().readValue(new URL(getUrl()),
        JsonNode.class);

      if (!node.isObject() || !node.has("status")) {
        getLogger(getClass()).warn("Unexpected dependency response at " + getUrl());
        return new TestResult(this, true, Status.UNKNOWN);
      }


      if ("healthy".equals(node.get("status").asText())) {
        return new TestResult(this, true, Status.ONLINE);
      }

      return new TestResult(this, true, Status.OFFLINE);

    } catch (IOException e) {
      getLogger(getClass()).warn("Failed to read dependency " + getUrl(), e);
    }
    return new TestResult(this, false, Status.UNKNOWN);
  }
}
