package org.veupathdb.service.userds.util;

import io.vulpine.lib.jcfi.CheckedRunnable;
import org.apache.logging.log4j.Logger;
import org.veupathdb.lib.container.jaxrs.providers.LogProvider;

public class Errors
{
  private static final Logger log = LogProvider.logger(Errors.class);

  public static void swallow(CheckedRunnable fn) {
    try {
      fn.run();
    } catch (Throwable e) {
      log.debug("Swallowing error: ", e);
    }
  }
}
