package com.globalpayments.scale.dto;

import com.globalpayments.scale.model.Feedback;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateFeedbackRequest {

    @NotBlank(message = "Idea ID is required")
    private String ideaId;

    @NotBlank(message = "Feedback text is required")
    @Size(min = 5, message = "Feedback text must be at least 5 characters")
    private String feedbackText;

    private Feedback.FeedbackType type = Feedback.FeedbackType.SUGGESTION;

    private Feedback.Visibility visibility = Feedback.Visibility.PUBLIC;

    private String badgesAwarded;
}