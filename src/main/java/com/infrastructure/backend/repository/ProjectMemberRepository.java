package com.infrastructure.backend.repository;

import com.infrastructure.backend.entity.project.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Integer> {

    Optional<ProjectMember> findByProject_IdAndUser_Id(Integer projectId, Integer userId);

    void deleteByProject_IdAndUser_Id(Integer projectId, Integer userId);

    List<ProjectMember> findAllByProject_Id(Integer projectId);

    List<ProjectMember> findAllByUser_Id(Integer userId);

}
