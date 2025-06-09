package com.globalpayments.scale.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEvaluationRequest {

    @NotBlank(message = "Idea ID is required")
    private String ideaId;

    @Min(value = 1, message = "ROI score must be at least 1")
    @Max(value = 10, message = "ROI score must not exceed 10")
    private Integer roiScore;

    @Min(value = 1, message = "Feasibility score must be at least 1")
    @Max(value = 10, message = "Feasibility score must not exceed 10")
    private Integer feasibilityScore;

    @Min(value = 1, message = "Alignment score must be at least 1")
    @Max(value = 10, message = "Alignment score must not exceed 10")
    private Integer alignmentScore;

    @Min(value = 1, message = "Risk score must be at least 1")
    @Max(value = 10, message = "Risk score must not exceed 10")
    private Integer riskScore;

    private BigDecimal implementationCost;

    @NotNull(message = "Total score is required")
    @Min(value = 1, message = "Total score must be at least 1")
    @Max(value = 100, message = "Total score must not exceed 100")
    private Integer totalScore;

    private String reviewComments;

    private Integer aiScoreSuggestion;
}