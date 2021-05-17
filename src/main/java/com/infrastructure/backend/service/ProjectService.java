package com.infrastructure.backend.service;

import com.infrastructure.backend.entity.project.Project;
import com.infrastructure.backend.model.common.request.ProjectCreateRequest;

import java.util.List;

public interface ProjectService {

    Project create(ProjectCreateRequest projectCreateRequest);

    Project update(int projectId, ProjectCreateRequest projectUpdateRequest);

    Project delete(int projectId);

    Project find(int projectId);

    List<Project> findAll();
}
