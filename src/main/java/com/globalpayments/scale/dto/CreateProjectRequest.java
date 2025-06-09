package com.globalpayments.scale.dto;

import com.globalpayments.scale.model.Project;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectRequest {

    @NotBlank(message = "Idea ID is required")
    private String ideaId;

    private Boolean mvpDefined = false;

    private String assignedTeam;

    private BigDecimal budgetAllocated;

    private String kpis;

    private Project.ProjectStatus status = Project.ProjectStatus.PLANNING;

    private LocalDate startDate;

    private LocalDate endDate;

    private String progressUpdates;

    private String aiSuggestedTechStack;

    private String documentationLink;
}
