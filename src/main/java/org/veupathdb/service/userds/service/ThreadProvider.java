package org.veupathdb.service.userds.service;

import org.apache.logging.log4j.ThreadContext;
import org.veupathdb.lib.container.jaxrs.Globals;

public class ThreadProvider
{
  public static Thread newThread(Runnable r) {
    final var cur = ThreadContext.get(Globals.CONTEXT_ID);
    return new Thread(() -> {
      ThreadContext.put(Globals.CONTEXT_ID, cur);
      r.run();
    });
  }
}
