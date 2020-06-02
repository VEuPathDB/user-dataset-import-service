package org.veupathdb.service.userds.model.config;

import org.veupathdb.service.userds.model.Service;

public class HandlerConfig
{
  private Service[] services;

  public Service[] getServices() {
    return services;
  }

  public void setServices(Service[] services) {
    this.services = services;
  }
}
