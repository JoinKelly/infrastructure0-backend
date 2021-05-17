package com.infrastructure.backend.repository;

import com.infrastructure.backend.entity.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
}
