package com.infrastructure.backend.repository;

import com.infrastructure.backend.entity.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    List<Project> findAllByLeader_Id(int leaderId);
}
