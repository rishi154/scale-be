package com.globalpayments.scale.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    private String projectId;
    private String ideaId;
    private Boolean mvpDefined;
    private String assignedTeam; // Stored as JSON string
    private BigDecimal budgetAllocated;
    private String kpis; // Stored as JSON string
    private ProjectStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private String progressUpdates; // Stored as JSON string
    private String aiSuggestedTechStack;
    private String documentationLink;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum ProjectStatus {
        PLANNING, IN_PROGRESS, COMPLETED, ON_HOLD
    }
}