package com.infrastructure.backend.service;

import com.infrastructure.backend.entity.task.Task;
import com.infrastructure.backend.model.task.request.TaskCreateRequest;

import java.util.List;

public interface TaskService {

    Task create(TaskCreateRequest taskCreateRequest);

    Task update(int taskId, TaskCreateRequest taskCreateRequest);

    void delete(int taskId);

    Task find(int taskId);

    List<Task> findAllByProject(int projectId);

    List<Task> findAllByUser(int userId);
}
