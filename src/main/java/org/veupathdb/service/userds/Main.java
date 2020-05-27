package org.veupathdb.service.userds;

import java.io.FileReader;
import java.util.Arrays;

import org.veupathdb.lib.container.jaxrs.config.Options;
import org.veupathdb.lib.container.jaxrs.health.Dependency;
import org.veupathdb.lib.container.jaxrs.health.ServiceDependency;
import org.veupathdb.lib.container.jaxrs.server.ContainerResources;
import org.veupathdb.lib.container.jaxrs.server.Server;
import org.veupathdb.service.userds.model.Config;
import org.veupathdb.service.userds.model.Service;
import org.veupathdb.service.userds.util.Json;

public class Main extends Server {
  public static Config jsonConfig;

  public static void main(String[] args) throws Exception {
    Json.FACTORY.readerFor(Config.class)
      .readTree(new FileReader("config.json"));

    var server = new Main();

    server.enableAccountDB();
    server.start(args);
  }

  @Override
  protected ContainerResources newResourceConfig(Options options) {
    final var out = new Resources(options);

    // Enabled by default for debugging purposes, this should be removed when
    // production ready.
    out.property("jersey.config.server.tracing.type", "ALL")
      .property("jersey.config.server.tracing.threshold", "VERBOSE");

    return out;
  }

  @Override
  protected Dependency[] dependencies() {
    return Arrays.stream(jsonConfig.getServices())
      .map(svc -> new ServiceDependency() {})
  }
}
