package org.veupathdb.service.userds.service;

import java.io.InputStream;
import java.nio.file.Paths;

import org.apache.logging.log4j.Logger;
import org.irods.jargon.core.connection.ClientServerNegotiationPolicy;
import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.packinstr.DataObjInp;
import org.irods.jargon.core.pub.IRODSFileSystem;
import org.veupathdb.lib.container.jaxrs.providers.LogProvider;
import org.veupathdb.service.userds.model.config.ExtOptions;

public class Irods
{
  private static final Logger log = LogProvider.logger(Irods.class);

  private static final String
    lzPath   = "/ebrc/workspaces/lz",
    flagPath = "/ebrc/workspaces/flags";

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
      log.debug("writing dataset {} to iRODS {}", fName, lzPath);
      pipe.transferTo(writer);
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

    csnp.setSslNegotiationPolicy(
      ClientServerNegotiationPolicy.SslNegotiationPolicy.CS_NEG_REFUSE);

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
