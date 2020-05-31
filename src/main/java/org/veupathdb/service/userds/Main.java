package org.veupathdb.service.userds;

import java.io.FileReader;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.veupathdb.lib.container.jaxrs.config.Options;
import org.veupathdb.lib.container.jaxrs.health.Dependency;
import org.veupathdb.lib.container.jaxrs.server.ContainerResources;
import org.veupathdb.lib.container.jaxrs.server.Server;
import org.veupathdb.service.userds.config.ExtOptions;
import org.veupathdb.service.userds.model.Config;
import org.veupathdb.service.userds.repo.SelectProjectQuery;
import org.veupathdb.service.userds.repo.SelectStatusQuery;
import org.veupathdb.service.userds.util.DatabaseDependency;
import org.veupathdb.service.userds.util.DbMan;
import org.veupathdb.service.userds.util.HandlerDependency;
import org.veupathdb.service.userds.util.Format;

public class Main extends Server {
  private static final Logger LOG = LogManager.getLogger(Main.class);

  public static final ExtOptions options = new ExtOptions();

  private static DataSource importDB;
  public static Config jsonConfig;

  public static void main(String[] args) throws Exception {
    LOG.info("Parsing service config");
    Format.Json.readerFor(Config.class)
      .readTree(new FileReader("config.json"));

    LOG.info("Initializing import datastore connection");
    importDB = DbMan.initImportDb();

    LOG.info("Populating type caches");
    SelectStatusQuery.run();
    SelectProjectQuery.run();

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
    final var deps = Arrays.stream(jsonConfig.getServices())
      .map(svc -> new HandlerDependency(svc.getDsType(), svc.getUrl(), 80))
      .map(Dependency.class::cast)
      .collect(Collectors.toList());

    deps.add(new DatabaseDependency("import-db", "postgres", (short) 5432,
      importDB));

    return deps.toArray(Dependency[]::new);
  }

  @Override
  protected Options newOptions() {
    return options;
  }
}
