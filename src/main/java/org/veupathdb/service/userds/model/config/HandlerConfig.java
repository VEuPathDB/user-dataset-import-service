package org.veupathdb.service.userds.model.config;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.veupathdb.service.userds.model.ProjectCache;
import org.veupathdb.service.userds.model.Service;

public class HandlerConfig
{
  private final List < Service > services;

  private final Set < String > projects;

  private final Map < String, Set < String > > byType;

  public HandlerConfig() {
    services = new ArrayList <>();
    projects = new HashSet <>();
    byType = new HashMap <>();
  }

  public List < Service > getServices() {
    return Collections.unmodifiableList(services);
  }

  public Set < String > getProjects() {
    return Collections.unmodifiableSet(projects);
  }

  public Map < String, Set < String > > getByType() {
    return Collections.unmodifiableMap(byType);
  }

  @JsonSetter
  public void setServices(Service[] services) {
    final var cache = ProjectCache.getInstance();

    for (final var svc : services) {
      // Validate config project
      for (final var pro : svc.getProjects()) {
        if (!cache.containsKey(pro))
          throw new RuntimeException("Invalid project in configuration: " + pro);
        projects.add(pro);
      }

      byType.computeIfAbsent(svc.getDsType(), __ -> new HashSet <>())
        .addAll(Arrays.asList(svc.getProjects()));

      this.services.add(svc);
    }

    // Lock down the byType sets
    byType.replaceAll((k, v) -> Collections.unmodifiableSet(byType.get(k)));
  }
}
