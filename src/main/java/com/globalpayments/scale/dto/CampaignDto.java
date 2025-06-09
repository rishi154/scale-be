package com.globalpayments.scale.dto;

import com.globalpayments.scale.model.Campaign;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignDto {
    private String campaignId;
    private String title;
    private String description;
    private String theme;
    private LocalDate startDate;
    private LocalDate endDate;
    private String ownerId;
    private String ownerName; // Additional field for owner's name
    private Campaign.CampaignStatus status;
    private String targetAudience;
    private String aiGeneratedContent;
    private int ideaCount; // Additional field for number of submitted ideas
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}