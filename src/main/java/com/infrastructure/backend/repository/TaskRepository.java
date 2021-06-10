package com.infrastructure.backend.repository;

import com.infrastructure.backend.entity.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findAllByProject_Id(Integer projectId);

    List<Task> findAllByProject_IdAndState(Integer projectId, String state);

    void deleteByProject_Id(Integer projectId);

    List<Task> findAllByProject_IdAndUser_Id(Integer projectId, Integer userId);

    List<Task> findAllByUser_Id(Integer userId);

    List<Task> findAllByUser_IdAndState(Integer userId, String state);

}
