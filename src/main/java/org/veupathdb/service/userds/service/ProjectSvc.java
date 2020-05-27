package org.veupathdb.service.userds.service;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.veupathdb.service.userds.Main;
import org.veupathdb.service.userds.generated.model.NotFoundErrorImpl;
import org.veupathdb.service.userds.generated.model.ProjectType;
import org.veupathdb.service.userds.generated.resources.Projects;
import org.veupathdb.service.userds.model.Service;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;

public class ProjectSvc implements Projects {

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
  public GetProjectsDatasetTypesByProjectTypeResponse getProjectsDatasetTypesByProjectType(
    ProjectType projectType
  ) {
    final var svcs = stream(Main.jsonConfig.getServices())
      .filter(svc -> asList(svc.getProjects()).contains(projectType.getName()))
      .toArray(Service[]::new);

    if (svcs.length == 0) {
      final var out = new NotFoundErrorImpl();
      out.setMessage("no configured handlers for " + projectType.getName());
      return GetProjectsDatasetTypesByProjectTypeResponse
        .respond404WithApplicationJson(out);
    }

    return GetProjectsDatasetTypesByProjectTypeResponse
      .respond200WithApplicationJson(stream(svcs)
        .map(Service::getDsType)
        .sorted()
        .distinct()
        .collect(Collectors.toList()));
  }

  @Override
  public GetProjectsDatasetTypesFileTypesByProjectTypeAndDatasetTypeResponse
  getProjectsDatasetTypesFileTypesByProjectTypeAndDatasetType(
    ProjectType projectType,
    String      datasetType
  ) {
    final var svcs = stream(Main.jsonConfig.getServices())
      .filter(svc -> datasetType.equals(svc.getDsType()))
      .filter(svc -> asList(svc.getProjects()).contains(projectType.getName()))
      .toArray(Service[]::new);

    if (svcs.length == 0) {
      final var out = new NotFoundErrorImpl();
      out.setMessage(format("dataset type %s not found for project type %s",
        datasetType, projectType.getName()));
      return GetProjectsDatasetTypesFileTypesByProjectTypeAndDatasetTypeResponse
        .respond404WithApplicationJson(out);
    }

    return GetProjectsDatasetTypesFileTypesByProjectTypeAndDatasetTypeResponse
      .respond200WithApplicationJson(stream(svcs)
        .map(Service::getFileTypes)
        .flatMap(Arrays::stream)
        .sorted()
        .distinct()
        .collect(Collectors.toList()));
  }
}
