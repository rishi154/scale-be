package com.globalpayments.scale.dto;

import com.globalpayments.scale.model.Idea;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateIdeaRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 255, message = "Title must be between 5 and 255 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 20, message = "Description must be at least 20 characters")
    private String description;

    private String department;

    private String tags; // JSON format

    private String attachments; // JSON format

    private Idea.IdeaStatus status = Idea.IdeaStatus.DRAFT;

    private String language;

    private String campaignId;
}