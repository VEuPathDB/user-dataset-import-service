package org.veupathdb.service.userds.service.meta;

import java.security.CodeSource;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import io.vulpine.lib.sql.load.SqlLoader;
import org.veupathdb.lib.container.jaxrs.providers.LogProvider;
import org.veupathdb.service.userds.Main;
import org.veupathdb.service.userds.repo.meta.SelectStoreExists;
import org.veupathdb.service.userds.repo.meta.SelectVersion;
import org.veupathdb.service.userds.util.DbMan;

public class Migrator
{
  private static final String migrationsDir = "/sql/migrations/";

  public static void run() throws Exception {
    final var log = LogProvider.logger(Main.class);

    log.info("Checking for database updates.");

    var version = SelectStoreExists.run()
      ? SelectVersion.run()
      : "v1.0.0";

    log.debug("starting with version " + version);

    var migs = getMigrations(version);

    if (migs.isEmpty()) {
      log.debug("no migrations found");
      return;
    }

    log.debug("found {} migrations", migs.size());

    try (var con = DbMan.getImportDb().getConnection()) {
      var loader = new SqlLoader(migrationsDir);
      for (var path : migs) {
        try (var stmt = con.createStatement()) {
          log.debug("executing {}{}.sql", migrationsDir, path);
          stmt.execute(loader.rawSql(path).orElseThrow());
        }
      }
    }
  }

  private static List < String > getMigrations(String ver) throws Exception {
    final var jar   = new ZipInputStream(getSelf().getLocation().openStream());
    final var paths = new ArrayList < String >(12);

    ZipEntry file;
    while ((file = jar.getNextEntry()) != null) {
      var path = file.getName();

      // Skip unrelated files and directories.
      if (!path.startsWith(migrationsDir) || !path.endsWith(".sql"))
        continue;

      // Chop off the /path/value/ and the next / character
      path = path.substring(migrationsDir.length());

      final var version = path.split("/")[0];

      // Skip migrations that are at or below the current version
      if (ver.compareTo(version) > -1)
        continue;

      path = path.replace(".sql", "");

      paths.add(path);
    }

    Collections.sort(paths);

    return paths;
  }

  /**
   * Returns a handle for the running jar itself.
   */
  private static CodeSource getSelf() {
    return Migrator.class.getProtectionDomain().getCodeSource();
  }
}
