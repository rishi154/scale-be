package com.globalpayments.scale.dto;

import com.globalpayments.scale.model.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {
    private String projectId;
    private String ideaId;
    private String ideaTitle; // Additional field for idea title
    private Boolean mvpDefined;
    private String assignedTeam;
    private BigDecimal budgetAllocated;
    private String kpis;
    private Project.ProjectStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private String progressUpdates;
    private String aiSuggestedTechStack;
    private String documentationLink;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}