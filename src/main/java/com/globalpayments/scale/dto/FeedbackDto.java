package com.globalpayments.scale.dto;

import com.globalpayments.scale.model.Feedback;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDto {
    private String feedbackId;
    private String ideaId;
    private String ideaTitle; // Additional field for idea title
    private String submitterId;
    private String submitterName; // Additional field for submitter's name
    private String feedbackText;
    private Feedback.FeedbackType type;
    private Feedback.Visibility visibility;
    private Integer recognitionPoints;
    private String badgesAwarded;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}