package com.globalpayments.scale.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {
    private String feedbackId;
    private String ideaId;
    private String submitterId;
    private String feedbackText;
    private FeedbackType type;
    private Visibility visibility;
    private Integer recognitionPoints;
    private String badgesAwarded; // Stored as JSON string
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum FeedbackType {
        PRAISE, SUGGESTION, CONCERN
    }

    public enum Visibility {
        PUBLIC, PRIVATE
    }
}