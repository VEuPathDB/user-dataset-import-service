package org.veupathdb.service.userds.model.handler;

import java.io.InputStream;

public class HandlerJobResult
{
  private final String fileName;
  private final InputStream content;

  public HandlerJobResult(String fileName, InputStream content) {
    this.fileName = fileName;
    this.content = content;
  }

  public String getFileName() {
    return fileName;
  }

  public InputStream getContent() {
    return content;
  }
}
