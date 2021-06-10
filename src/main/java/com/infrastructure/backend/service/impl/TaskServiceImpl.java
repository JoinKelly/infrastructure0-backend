package com.infrastructure.backend.service.impl;

import com.infrastructure.backend.common.exception.CustomResponseStatusException;
import com.infrastructure.backend.common.exception.ErrorCode;
import com.infrastructure.backend.entity.project.Project;
import com.infrastructure.backend.entity.task.Task;
import com.infrastructure.backend.entity.task.TaskFetchMode;
import com.infrastructure.backend.entity.task.TaskState;
import com.infrastructure.backend.entity.user.User;
import com.infrastructure.backend.model.task.request.TaskCreateRequest;
import com.infrastructure.backend.repository.ProjectRepository;
import com.infrastructure.backend.repository.TaskRepository;
import com.infrastructure.backend.repository.UserRepository;
import com.infrastructure.backend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Task create(TaskCreateRequest taskCreateRequest) {

        Project project = this.projectRepository.findById(taskCreateRequest.getProjectId()).orElseThrow(() -> new CustomResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.PROJECT_NOT_EXIST.name(), "Project is not exist"));
        User dbUser = null;

        if (taskCreateRequest.getUserId() != null) {
            dbUser = this.userRepository.findById(taskCreateRequest.getUserId()).orElseThrow(() -> new CustomResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_EXIST.name(), "User is not exist"));
        }
        Task task = new Task();
        task.setTitle(taskCreateRequest.getTitle());
        task.setDescription(taskCreateRequest.getDescription());
        task.setProject(project);
        task.setDeadLine(taskCreateRequest.getDeadLine());
        if (dbUser != null) {
            task.setUser(dbUser);
        }

        return this.taskRepository.save(task);
    }

    @Override
    public Task update(int taskId, TaskCreateRequest taskCreateRequest) {

        Task task = this.taskRepository.findById(taskId).orElseThrow(() -> new CustomResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.TASK_NOT_EXIST.name(), "Task is not exist"));

        Project project = this.projectRepository.findById(taskCreateRequest.getProjectId()).orElseThrow(() -> new CustomResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.PROJECT_NOT_EXIST.name(), "Project is not exist"));
        User dbUser = null;

        if (taskCreateRequest.getUserId() != null) {
            dbUser = this.userRepository.findById(taskCreateRequest.getUserId()).orElseThrow(() -> new CustomResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_EXIST.name(), "User is not exist"));
        }

        task.setTitle(taskCreateRequest.getTitle());
        task.setDescription(taskCreateRequest.getDescription());
        task.setProject(project);
        task.setDeadLine(taskCreateRequest.getDeadLine());
        if (dbUser != null) {
            task.setUser(dbUser);
        }

        return this.taskRepository.save(task);
    }

    @Override
    public void delete(int taskId) {
        Task task = this.taskRepository.findById(taskId).orElseThrow(() -> new CustomResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.TASK_NOT_EXIST.name(), "Task is not exist"));
        this.taskRepository.delete(task);
    }

    @Override
    public Task find(int taskId) {
        return this.taskRepository.findById(taskId).orElseThrow(() -> new CustomResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.TASK_NOT_EXIST.name(), "Task is not exist"));
    }

    @Override
    public List<Task> findAllByProject(int projectId, String fetchMode) {

        if (!StringUtils.hasText(fetchMode)) {
            return this.taskRepository.findAllByProject_Id(projectId);
        }

        TaskFetchMode taskFetchMode = TaskFetchMode.valueOf(fetchMode);
        switch (taskFetchMode) {
            case ALL:
                return this.taskRepository.findAllByProject_Id(projectId);
            case UN_ASSIGNED:
                return this.taskRepository.findAllByProject_IdAndUser_Id(projectId, null);
            case OPEN:
                return this.taskRepository.findAllByProject_IdAndState(projectId, TaskState.OPEN.name());
            case COMPLETE:
                return this.taskRepository.findAllByProject_IdAndState(projectId, TaskState.COMPLETE.name());
            case PROGRESS:
                return this.taskRepository.findAllByProject_IdAndState(projectId, TaskState.PROGRESS.name());
            case PENDING:
                return this.taskRepository.findAllByProject_IdAndState(projectId, TaskState.PENDING.name());
        }

        return null;
    }

    @Override
    public List<Task> findAllByUser(int userId, String fetchMode) {
        if (!StringUtils.hasText(fetchMode)) {
            return this.taskRepository.findAllByUser_Id(userId);
        }

        TaskFetchMode taskFetchMode = TaskFetchMode.valueOf(fetchMode);
        switch (taskFetchMode) {
            case ALL:
                return this.taskRepository.findAllByUser_Id(userId);
            case UN_ASSIGNED:
                return this.taskRepository.findAllByUser_Id(userId);
            case OPEN:
                return this.taskRepository.findAllByUser_IdAndState(userId, TaskState.OPEN.name());
            case COMPLETE:
                return this.taskRepository.findAllByUser_IdAndState(userId, TaskState.COMPLETE.name());
            case PROGRESS:
                return this.taskRepository.findAllByUser_IdAndState(userId, TaskState.PROGRESS.name());
            case PENDING:
                return this.taskRepository.findAllByUser_IdAndState(userId, TaskState.PENDING.name());
        }

        return new ArrayList<>();

    }

    @Override
    public void unAssignTasks(Integer projectsId, Integer userId) {
        List<Task> tasks = this.taskRepository.findAllByProject_IdAndUser_Id(projectsId, userId);
        if (tasks != null && tasks.size() > 0) {
            for (Task task : tasks) {
                task.setUser(null);
            }
            this.taskRepository.saveAll(tasks);
        }
    }

    @Override
    public Task changeState(int taskId, TaskState taskState) {
        Task task = this.taskRepository.findById(taskId).orElseThrow(() -> new CustomResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.TASK_NOT_EXIST.name(), "Task is not exist"));
        task.setState(taskState);
        return this.taskRepository.save(task);
    }
}
