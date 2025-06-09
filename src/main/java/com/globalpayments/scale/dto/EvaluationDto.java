package com.globalpayments.scale.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationDto {
    private String evaluationId;
    private String ideaId;
    private String ideaTitle; // Additional field for idea title
    private String reviewerId;
    private String reviewerName; // Additional field for reviewer's name
    private LocalDateTime date;
    private Integer roiScore;
    private Integer feasibilityScore;
    private Integer alignmentScore;
    private Integer riskScore;
    private BigDecimal implementationCost;
    private Integer totalScore;
    private String reviewComments;
    private Integer aiScoreSuggestion;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}