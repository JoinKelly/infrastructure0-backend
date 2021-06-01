package com.infrastructure.backend.repository;

import com.infrastructure.backend.entity.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}
