package com.globalpayments.scale.service;

import com.globalpayments.scale.dao.IdeaDao;
import com.globalpayments.scale.dao.ProjectDao;
import com.globalpayments.scale.dto.CreateProjectRequest;
import com.globalpayments.scale.dto.ProjectDto;
import com.globalpayments.scale.exception.ResourceNotFoundException;
import com.globalpayments.scale.model.Idea;
import com.globalpayments.scale.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectDao projectDao;
    private final IdeaDao ideaDao;

    @Autowired
    public ProjectService(ProjectDao projectDao, IdeaDao ideaDao) {
        this.projectDao = projectDao;
        this.ideaDao = ideaDao;
    }

    public ProjectDto createProject(CreateProjectRequest request) {
        // Check if idea exists and update its status
        Idea idea = ideaDao.findById(request.getIdeaId())
                .orElseThrow(() -> new ResourceNotFoundException("Idea not found with ID: " + request.getIdeaId()));

        // Update idea status to IN_PROGRESS
        ideaDao.updateStatus(idea.getIdeaId(), Idea.IdeaStatus.IN_PROGRESS);

        // Update idea stage to LAUNCH
        ideaDao.updateStage(idea.getIdeaId(), Idea.IdeaStage.LAUNCH);

        // Check if a project already exists for this idea
        if (projectDao.findByIdeaId(request.getIdeaId()).isPresent()) {
            throw new IllegalArgumentException("A project already exists for idea with ID: " + request.getIdeaId());
        }

        Project project = new Project();
        project.setIdeaId(request.getIdeaId());
        project.setMvpDefined(request.getMvpDefined());
        project.setAssignedTeam(request.getAssignedTeam());
        project.setBudgetAllocated(request.getBudgetAllocated());
        project.setKpis(request.getKpis());
        project.setStatus(request.getStatus());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());
        project.setProgressUpdates(request.getProgressUpdates());
        project.setAiSuggestedTechStack(request.getAiSuggestedTechStack());
        project.setDocumentationLink(request.getDocumentationLink());

        String projectId = projectDao.create(project);

        return projectDao.findById(projectId)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Failed to create project"));
    }

    public ProjectDto getProjectById(String projectId) {
        Project project = projectDao.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + projectId));

        return mapToDto(project);
    }

    public ProjectDto getProjectByIdeaId(String ideaId) {
        Project project = projectDao.findByIdeaId(ideaId)
                .orElseThrow(() -> new ResourceNotFoundException("No project found for idea with ID: " + ideaId));

        return mapToDto(project);
    }

    public List<ProjectDto> getProjectsByStatus(Project.ProjectStatus status) {
        return projectDao.findByStatus(status).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ProjectDto> getAllProjects() {
        return projectDao.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ProjectDto updateProject(String projectId, CreateProjectRequest request) {
        Project project = projectDao.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + projectId));

        // Cannot change the idea associated with a project
        if (!project.getIdeaId().equals(request.getIdeaId())) {
            throw new IllegalArgumentException("Cannot change the idea associated with a project");
        }

        if (request.getMvpDefined() != null) {
            project.setMvpDefined(request.getMvpDefined());
        }

        if (request.getAssignedTeam() != null) {
            project.setAssignedTeam(request.getAssignedTeam());
        }

        if (request.getBudgetAllocated() != null) {
            project.setBudgetAllocated(request.getBudgetAllocated());
        }

        if (request.getKpis() != null) {
            project.setKpis(request.getKpis());
        }

        if (request.getStatus() != null) {
            project.setStatus(request.getStatus());

            // If project is completed, update idea status
            if (request.getStatus() == Project.ProjectStatus.COMPLETED) {
                ideaDao.updateStatus(project.getIdeaId(), Idea.IdeaStatus.LAUNCHED);
                ideaDao.updateStage(project.getIdeaId(), Idea.IdeaStage.EMBED);
            }
        }

        if (request.getStartDate() != null) {
            project.setStartDate(request.getStartDate());
        }

        if (request.getEndDate() != null) {
            project.setEndDate(request.getEndDate());
        }

        if (request.getProgressUpdates() != null) {
            project.setProgressUpdates(request.getProgressUpdates());
        }

        if (request.getAiSuggestedTechStack() != null) {
            project.setAiSuggestedTechStack(request.getAiSuggestedTechStack());
        }

        if (request.getDocumentationLink() != null) {
            project.setDocumentationLink(request.getDocumentationLink());
        }

        projectDao.update(project);

        return mapToDto(project);
    }

    public ProjectDto updateProjectStatus(String projectId, Project.ProjectStatus status) {
        if (!projectDao.updateStatus(projectId, status)) {
            throw new ResourceNotFoundException("Project not found with ID: " + projectId);
        }

        // If project is completed, update idea status
        if (status == Project.ProjectStatus.COMPLETED) {
            Project project = projectDao.findById(projectId)
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + projectId));

            ideaDao.updateStatus(project.getIdeaId(), Idea.IdeaStatus.LAUNCHED);
            ideaDao.updateStage(project.getIdeaId(), Idea.IdeaStage.EMBED);
        }

        return projectDao.findById(projectId)
                .map(this::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + projectId));
    }

    public void deleteProject(String projectId) {
        if (!projectDao.delete(projectId)) {
            throw new ResourceNotFoundException("Project not found with ID: " + projectId);
        }
    }

    private ProjectDto mapToDto(Project project) {
        ProjectDto dto = new ProjectDto();
        dto.setProjectId(project.getProjectId());
        dto.setIdeaId(project.getIdeaId());
        dto.setMvpDefined(project.getMvpDefined());
        dto.setAssignedTeam(project.getAssignedTeam());
        dto.setBudgetAllocated(project.getBudgetAllocated());
        dto.setKpis(project.getKpis());
        dto.setStatus(project.getStatus());
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());
        dto.setProgressUpdates(project.getProgressUpdates());
        dto.setAiSuggestedTechStack(project.getAiSuggestedTechStack());
        dto.setDocumentationLink(project.getDocumentationLink());
        dto.setCreatedAt(project.getCreatedAt());
        dto.setUpdatedAt(project.getUpdatedAt());

        // Get idea title if available
        ideaDao.findById(project.getIdeaId())
                .ifPresent(idea -> dto.setIdeaTitle(idea.getTitle()));

        return dto;
    }
}