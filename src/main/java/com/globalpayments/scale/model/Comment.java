package com.globalpayments.scale.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private String commentId;
    private String ideaId;
    private String submitterId;
    private String commentText;
    private CommentType type;
    private Visibility visibility;
    private Integer recognitionPoints;
    private String badgesAwarded;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public enum CommentType {
        PRAISE,
        SUGGESTION,
        CONCERN
    }
    public enum Visibility {
        PUBLIC,
        PRIVATE
    }
}