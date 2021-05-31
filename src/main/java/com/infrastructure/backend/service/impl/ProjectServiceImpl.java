package com.infrastructure.backend.service.impl;

import com.infrastructure.backend.common.exception.CustomResponseStatusException;
import com.infrastructure.backend.common.exception.ErrorCode;
import com.infrastructure.backend.entity.project.Project;
import com.infrastructure.backend.entity.project.ProjectMember;
import com.infrastructure.backend.entity.user.User;
import com.infrastructure.backend.model.project.request.ProjectCreateRequest;
import com.infrastructure.backend.repository.ProjectMemberRepository;
import com.infrastructure.backend.repository.ProjectRepository;
import com.infrastructure.backend.repository.UserRepository;
import com.infrastructure.backend.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Override
    public Project create(ProjectCreateRequest projectCreateRequest) {

        Project project = new Project();
        project.setTitle(projectCreateRequest.getTitle());
        project.setDescription(projectCreateRequest.getDescription());
        project.setStartDate(projectCreateRequest.getStartDate());
        project.setEndDate(projectCreateRequest.getEndDate());

        Integer leaderId = projectCreateRequest.getLeaderId();
        if (leaderId != null) {
            User dbUser = this.userRepository.findById(leaderId).orElseThrow(() -> new CustomResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_EXIST.name(), "Leader is not exist"));
            project.setLeader(dbUser);
        }
        return this.projectRepository.save(project);
    }

    @Override
    public Project update(int projectId, ProjectCreateRequest projectUpdateRequest) {
        Project project = this.projectRepository.findById(projectId).orElseThrow(() -> new CustomResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.PROJECT_NOT_EXIST.name(), "Project is not exist"));
        project.setTitle(projectUpdateRequest.getTitle());
        project.setDescription(projectUpdateRequest.getDescription());
        project.setStartDate(projectUpdateRequest.getStartDate());
        project.setEndDate(projectUpdateRequest.getEndDate());

        Integer leaderId = projectUpdateRequest.getLeaderId();
        if (leaderId != null) {
            if (project.getLeader() == null || !leaderId.equals(project.getLeader().getId())) {
                User dbUser = this.userRepository.findById(leaderId).orElseThrow(() -> new CustomResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_EXIST.name(), "Leader is not exist"));
                project.setLeader(dbUser);
            }

        } else {
            project.setLeader(null);
        }
        return this.projectRepository.save(project);
    }

    @Override
    public Project delete(int projectId) {
        Project project = this.projectRepository.findById(projectId).orElseThrow(() -> new CustomResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.PROJECT_NOT_EXIST.name(), "Project is not exist"));
        this.projectRepository.delete(project);
        Project deletedProject = new Project();
        deletedProject.setId(project.getId());
        return deletedProject;
    }

    @Override
    public Project find(int projectId) {
        return this.projectRepository.findById(projectId).orElseThrow(() -> new CustomResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.PROJECT_NOT_EXIST.name(), "Project is not exist"));
    }

    @Override
    public List<Project> findAll() {
        return this.projectRepository.findAll();
    }

    @Override
    public ProjectMember addProjectMember(Integer projectId, Integer userId) {
        Project project = this.projectRepository.findById(projectId).orElseThrow(() -> new CustomResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.PROJECT_NOT_EXIST.name(), "Project is not exist"));
        User dbUser = this.userRepository.findById(userId).orElseThrow(() -> new CustomResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_EXIST.name(), "User is not exist"));

        Optional<ProjectMember> checkProjectMember = this.projectMemberRepository.findByProject_IdAndUser_Id(projectId, userId);
        if (!checkProjectMember.isPresent()) {
            ProjectMember projectMember = new ProjectMember();
            projectMember.setProject(project);
            projectMember.setUser(dbUser);
            return this.projectMemberRepository.save(projectMember);
        }

        return checkProjectMember.get();
    }

    @Override
    public void deleteProjectMember(Integer projectId, Integer userId) {
        this.projectMemberRepository.deleteByProject_IdAndUser_Id(projectId, userId);
    }

    @Override
    public List<ProjectMember> findAllProjectMembersByProject(Integer projectId) {
        return this.projectMemberRepository.findAllByProject_Id(projectId);
    }

    @Override
    public ProjectMember addProjectMemberByEmail(int projectId, String email) {
        Project project = this.projectRepository.findById(projectId).orElseThrow(() -> new CustomResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.PROJECT_NOT_EXIST.name(), "Project is not exist"));
        User dbUser = this.userRepository.findByEmail(email).orElseThrow(() -> new CustomResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_EXIST.name(), "User is not exist"));

        Optional<ProjectMember> checkProjectMember = this.projectMemberRepository.findByProject_IdAndUser_Id(projectId, dbUser.getId());
        if (!checkProjectMember.isPresent()) {
            ProjectMember projectMember = new ProjectMember();
            projectMember.setProject(project);
            projectMember.setUser(dbUser);
            return this.projectMemberRepository.save(projectMember);
        }

        return checkProjectMember.get();
    }
}
