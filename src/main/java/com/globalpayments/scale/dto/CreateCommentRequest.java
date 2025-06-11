package com.globalpayments.scale.dto;
import com.globalpayments.scale.model.Comment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class CreateCommentRequest {
    @NotBlank(message = "Idea required")
    private String ideaId;

    @NotBlank(message = "Comment required") @Size(min = 5, message = "Comment text must be at least 5 characters")
    private String commentText;
    private Comment.CommentType type = Comment.CommentType.SUGGESTION;
    private Comment.Visibility visibility = Comment.Visibility.PUBLIC;
    private String badgesAwarded;
}