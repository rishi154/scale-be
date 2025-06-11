package com.globalpayments.scale.dto;
import com.globalpayments.scale.model.Comment;
import com.globalpayments.scale.model.Feedback;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private String commentId;
    private String ideaId;
    private String ideaTitle;
    private String submitterId;
    private String submitterName;
    private String commentText;
    private Comment.CommentType type;
    private Comment.Visibility visibility;
    private Integer recognitionPoints;
    private String badgesAwarded;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}