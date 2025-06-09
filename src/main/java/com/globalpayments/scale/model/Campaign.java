package com.globalpayments.scale.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Campaign {
    private String campaignId;
    private String title;
    private String description;
    private String theme;
    private LocalDate startDate;
    private LocalDate endDate;
    private String ownerId;
    private CampaignStatus status;
    private String targetAudience;
    private String aiGeneratedContent; // Stored as JSON string
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum CampaignStatus {
        DRAFT, ACTIVE, COMPLETED, CANCELLED
    }
}