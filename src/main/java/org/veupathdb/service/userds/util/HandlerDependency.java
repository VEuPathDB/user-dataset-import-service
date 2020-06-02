package org.veupathdb.service.userds.util;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;

import org.veupathdb.lib.container.jaxrs.health.ServiceDependency;

import static org.apache.logging.log4j.LogManager.getLogger;
import static org.veupathdb.service.userds.util.Format.Json;

public class HandlerDependency extends ServiceDependency {

  private static final String health = "health";

  public HandlerDependency(String dsType, String name, int port) {
    super(dsType, name, port);
  }

  @Override
  protected TestResult serviceTest() {
    try {
      final var res  = HttpClient.newHttpClient()
        .send(HttpRequest.newBuilder(URI.create(healthEndpoint()))
          .GET()
          .build(), HttpResponse.BodyHandlers.ofByteArray());
      final var node = Json.reader().readValue(res.body(), JsonNode.class);

      if (!node.isObject() || !node.has("status")) {
        getLogger(getClass()).warn("Unexpected dependency response at " + getUrl());
        return new TestResult(this, true, Status.UNKNOWN);
      }


      if ("healthy".equals(node.get("status").asText())) {
        return new TestResult(this, true, Status.ONLINE);
      }

      return new TestResult(this, true, Status.OFFLINE);

    } catch (Exception e) {
      getLogger(getClass()).warn("Failed to read dependency " + getUrl(), e);
    }
    return new TestResult(this, false, Status.UNKNOWN);
  }

  private String healthEndpoint() {
    return "http://" + (getUrl().endsWith("/")
      ? getUrl() + health
      : getUrl() + "/" + health);
  }
}
