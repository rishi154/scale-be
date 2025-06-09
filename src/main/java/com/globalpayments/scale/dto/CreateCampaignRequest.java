package com.globalpayments.scale.dto;

import com.globalpayments.scale.model.Campaign;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCampaignRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 255, message = "Title must be between 5 and 255 characters")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    private String theme;

    private LocalDate startDate;

    @Future(message = "End date must be in the future")
    private LocalDate endDate;

    private Campaign.CampaignStatus status = Campaign.CampaignStatus.DRAFT;

    private String targetAudience;

    private String aiGeneratedContent;
}