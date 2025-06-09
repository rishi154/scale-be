package com.globalpayments.scale.dao;

import com.globalpayments.scale.model.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectDao {
    String create(Project project);
    Optional<Project> findById(String projectId);
    Optional<Project> findByIdeaId(String ideaId);
    List<Project> findByStatus(Project.ProjectStatus status);
    List<Project> findAll();
    void update(Project project);
    boolean updateStatus(String projectId, Project.ProjectStatus status);
    boolean delete(String projectId);
}