package com.infrastructure.backend.repository;

import com.infrastructure.backend.entity.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findAllByProject_Id(Integer projectId);

    List<Task> findAllByUser_Id(Integer userId);

}
