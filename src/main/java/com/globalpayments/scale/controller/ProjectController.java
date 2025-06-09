package com.globalpayments.scale.controller;

import com.globalpayments.scale.dto.CreateProjectRequest;
import com.globalpayments.scale.dto.ProjectDto;
import com.globalpayments.scale.model.Project;
import com.globalpayments.scale.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ProjectDto> createProject(@Valid @RequestBody CreateProjectRequest request) {
        return new ResponseEntity<>(projectService.createProject(request), HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDto> getProjectById(@PathVariable String projectId) {
        return ResponseEntity.ok(projectService.getProjectById(projectId));
    }

    @GetMapping("/idea/{ideaId}")
    public ResponseEntity<ProjectDto> getProjectByIdeaId(@PathVariable String ideaId) {
        return ResponseEntity.ok(projectService.getProjectByIdeaId(ideaId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ProjectDto>> getProjectsByStatus(@PathVariable Project.ProjectStatus status) {
        return ResponseEntity.ok(projectService.getProjectsByStatus(status));
    }

    @GetMapping
    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectDto> updateProject(@PathVariable String projectId,
                                                    @Valid @RequestBody CreateProjectRequest request) {
        return ResponseEntity.ok(projectService.updateProject(projectId, request));
    }

    @PutMapping("/{projectId}/status")
    public ResponseEntity<ProjectDto> updateProjectStatus(@PathVariable String projectId,
                                                          @RequestParam Project.ProjectStatus status) {
        return ResponseEntity.ok(projectService.updateProjectStatus(projectId, status));
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable String projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }
}
