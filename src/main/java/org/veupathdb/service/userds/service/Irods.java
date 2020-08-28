package org.veupathdb.service.userds.service;

import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Optional;

import org.apache.logging.log4j.Logger;
import org.irods.jargon.core.connection.ClientServerNegotiationPolicy;
import org.irods.jargon.core.connection.ClientServerNegotiationPolicy.SslNegotiationPolicy;
import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.packinstr.DataObjInp;
import org.irods.jargon.core.pub.IRODSFileSystem;
import org.veupathdb.lib.container.jaxrs.providers.LogProvider;
import org.veupathdb.service.userds.model.IrodsStatus;
import org.veupathdb.service.userds.model.config.ExtOptions;

public class Irods
{
  private static final int bufferSize = 32_768;
  private static final int flushCount = 10;  // flush every 160KiB
  private static final int noteCount  = 640; // log every 10MiB
  private static final Logger log = LogProvider.logger(Irods.class);

  private static final String
    lzPath   = "/ebrc/workspaces/lz",
    flagPath = "/ebrc/workspaces/flags",
    userPath = "/ebrc/workspaces/users";

  private static final String
    successFlagPrefix = "success_",
    failureFlagPrefix = "failure_";

  private static IRODSAccount    account;
  private static IRODSFileSystem system;

  public static void writeDataset(String fName, InputStream pipe)
  throws Exception {
    log.trace("Irods#writeDataset");
    var fs = system.getIRODSFileFactory(account);

    try (var writer = fs.instanceIRODSFileOutputStream(
      Paths.get(lzPath, fName).toString(),
      DataObjInp.OpenFlags.WRITE_FAIL_IF_EXISTS
    )) {
      var buffer = new byte[bufferSize];
      var counter = 0;
      log.debug("writing dataset {} to iRODS {}", fName, lzPath);
      int len;
      while ((len = pipe.read(buffer)) > -1) {
        writer.write(buffer, 0, len);
        counter++;

        if (counter % flushCount == 0)
          writer.flush();

        if (counter % noteCount == 0)
          log.trace((counter * bufferSize / 1024 / 1024) + "MiB written to iRODS");
      }
      writer.flush();
    }

    var flag = Paths.get(flagPath, fName.replace(".tgz", ".txt")).toString();
    var file = fs.instanceIRODSFile(flag);

    log.debug("writing flag {} to iRODS {}", flag, flagPath);

    try {
      file.createNewFile();
    } finally {
      file.close();
    }
  }

  public static Optional < IrodsStatus > getFlag(String fName)
  throws Exception {
    log.trace("Irods#getFlag");
    var fs = system.getIRODSFileFactory(account);

    var file = fs.instanceIRODSFile(flagPath, successFlagPrefix + fName);
    try {
      if (file.exists()) {
        try (var is = fs.instanceIRODSFileInputStream(file)) {
          var rawBody = new String(is.readAllBytes());
          var dsId = Integer.parseInt(
            Paths.get(rawBody.split(userPath)[1].split(" ")[0])
              .getFileName().toString());
          return Optional.of(IrodsStatus.success(dsId));
        }
      }
    } finally {
      file.close();
    }

    file = fs.instanceIRODSFile(flagPath, failureFlagPrefix + fName);
    try {
      if (file.exists())
        return Optional.of(IrodsStatus.failure());
    } finally {
      file.close();
    }

    return Optional.empty();
  }

  public static void initialize(ExtOptions opts) {
    log.trace("Irods#initialize");
    try {
      account = initAccount(opts);
      system = initSystem();
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  private static IRODSFileSystem initSystem() throws Exception {
    log.trace("Irods#initSystem");
    return IRODSFileSystem.instance();
  }

  /**
   * Initialize an iRODS account object.
   */
  private static IRODSAccount initAccount(ExtOptions opts) throws Exception {
    log.trace("Irods#initAccount");
    final var csnp = new ClientServerNegotiationPolicy();

    csnp.setSslNegotiationPolicy(opts.getIrodsSsl()
      .orElse(SslNegotiationPolicy.CS_NEG_REQUIRE));

    return IRODSAccount.instance(
      opts.getIrodsHost().orElseThrow(),
      opts.getIrodsPort().orElseThrow(),
      opts.getIrodsUser().orElseThrow(),
      opts.getIrodsPass().orElseThrow(),
      String.format(
        "/%s/home/%s",
        opts.getIrodsZone().orElseThrow(),
        opts.getIrodsUser().get()
      ),
      opts.getIrodsZone().get(),
      opts.getIrodsResource().orElseThrow(),
      csnp
    );
  }
}
