package com.globalpayments.scale.dto;

import com.globalpayments.scale.model.Idea;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdeaDto {
    private String ideaId;
    private String title;
    private String description;
    private String submittedBy;
    private String submitterName; // Additional field to display submitter's name
    private LocalDateTime submissionDate;
    private String department;
    private String tags;
    private String attachments;
    private Integer votesCount;
    private Integer viewCount;
    private Idea.IdeaStatus status;
    private Idea.IdeaStage stage;
    private String language;
    private String aiSummary;
    private String aiClusterId;
    private String campaignId;
    private String campaignTitle; // Additional field to display campaign title
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}