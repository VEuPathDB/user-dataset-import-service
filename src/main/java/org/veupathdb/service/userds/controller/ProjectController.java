package org.veupathdb.service.userds.controller;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.veupathdb.service.userds.Main;
import org.veupathdb.service.userds.generated.model.NotFoundErrorImpl;
import org.veupathdb.service.userds.generated.resources.Projects;
import org.veupathdb.service.userds.model.Service;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;

public class ProjectController implements Projects {
  private static final String[] BASE_FILE_TYPES = {"zip", "tar.gz", "tgz"};

  @Override
  public GetProjectsResponse getProjects() {
    return GetProjectsResponse.respond200WithApplicationJson(
      stream(Main.jsonConfig.getServices())
        .map(Service::getProjects)
        .flatMap(Arrays::stream)
        .distinct()
        .sorted()
        .collect(Collectors.toList()));
  }

  @Override
  public GetProjectHandlers
  getProjectHandlers(String project) {
    final var svcs = stream(Main.jsonConfig.getServices())
      .filter(svc -> asList(svc.getProjects()).contains(project))
      .toArray(Service[]::new);

    if (svcs.length == 0) {
      final var out = new NotFoundErrorImpl();
      out.setMessage("no configured handlers for " + project);
      return GetProjectHandlers
        .respond404(out);
    }

    return GetProjectHandlers
      .respond200(stream(svcs)
        .map(Service::getDsType)
        .sorted()
        .distinct()
        .collect(Collectors.toList()));
  }

  @Override
  public GetHandlerFileTypes
  getHandlerFileTypes(
    String project,
    String dsType
  ) {
    final var svcs = stream(Main.jsonConfig.getServices())
      .filter(svc -> dsType.equals(svc.getDsType()))
      .filter(svc -> asList(svc.getProjects()).contains(project))
      .toArray(Service[]::new);

    if (svcs.length == 0) {
      final var out = new NotFoundErrorImpl();
      out.setMessage(format("dataset type %s not found for project type %s",
        dsType, project));
      return GetHandlerFileTypes
        .respond404(out);
    }

    return GetHandlerFileTypes
      .respond200(Stream.concat(stream(svcs)
        .map(Service::getFileTypes)
        .flatMap(Arrays::stream), Stream.of(BASE_FILE_TYPES))
        .sorted()
        .distinct()
        .collect(Collectors.toList()));
  }
}
