package com.infrastructure.backend.service;

import com.infrastructure.backend.entity.project.Project;
import com.infrastructure.backend.entity.project.ProjectMember;
import com.infrastructure.backend.model.project.request.ProjectCreateRequest;

import java.util.List;

public interface ProjectService {

    Project create(ProjectCreateRequest projectCreateRequest);

    Project update(int projectId, ProjectCreateRequest projectUpdateRequest);

    Project delete(int projectId);

    Project find(int projectId);

    List<Project> findAll();

    ProjectMember addProjectMember(Integer projectId, Integer userId);

    void deleteProjectMember(Integer projectId, Integer userId);

    List<ProjectMember> findAllProjectMembersByProject(Integer projectId);

    ProjectMember addProjectMemberByEmail(int projectId, String email);
}
