package com.globalpayments.scale.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Idea {
    private String ideaId;
    private String title;
    private String description;
    private String submittedBy;
    private LocalDateTime submissionDate;
    private String department;
    private String tags; // Stored as JSON string
    private String attachments; // Stored as JSON string
    private Integer votesCount;
    private Integer viewCount;
    private IdeaStatus status;
    private IdeaStage stage;
    private String language;
    private String aiSummary;
    private String aiClusterId;
    private String campaignId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum IdeaStatus {
        DRAFT, SUBMITTED, UNDER_REVIEW, APPROVED, REJECTED, IN_PROGRESS, LAUNCHED, EMBEDDED
    }

    public enum IdeaStage {
        SOLICIT, CURATE, ASSESS, LAUNCH, EMBED
    }
}