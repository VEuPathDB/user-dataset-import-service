package org.veupathdb.service.userds.model.config;

import java.util.Optional;

import org.veupathdb.lib.container.jaxrs.config.Options;
import picocli.CommandLine.Option;

public class ExtOptions extends Options
{
  @Option(
    names = "--irods-host",
    defaultValue = "${env:IRODS_HOST}",
    description = "env: IRODS_HOST",
    arity = "1")
  private String irodsHost;

  @Option(
    names = "--irods-port",
    defaultValue = "${env:IRODS_PORT}",
    description = "env: IRODS_PORT",
    arity = "1")
  private Integer irodsPort;

  @Option(
    names = "--irods-user",
    defaultValue = "${env:IRODS_USER}",
    description = "env: IRODS_USER",
    arity = "1")
  private String irodsUser;

  @Option(
    names = "--irods-zone",
    defaultValue = "${env:IRODS_ZONE}",
    description = "env: IRODS_ZONE",
    arity = "1")
  private String irodsZone;

  @Option(
    names = "--irods-pass",
    defaultValue = "${env:IRODS_PASS}",
    description = "env: IRODS_PASS",
    arity = "1")
  private String irodsPass;

  @Option(
    names = "--irods-resource",
    defaultValue = "${env:IRODS_RESOURCE}",
    description = "env: IRODS_RESOURCE",
    arity = "1")
  private String irodsResource;

  public Optional < String > getIrodsHost() {
    return Optional.ofNullable(irodsHost);
  }

  public Optional < Integer > getIrodsPort() {
    return Optional.ofNullable(irodsPort);
  }

  public Optional < String > getIrodsUser() {
    return Optional.ofNullable(irodsUser);
  }

  public Optional < String > getIrodsZone() {
    return Optional.ofNullable(irodsZone);
  }

  public Optional < String > getIrodsPass() {
    return Optional.ofNullable(irodsPass);
  }

  public Optional < String > getIrodsResource() {
    return Optional.ofNullable(irodsResource);
  }
}
