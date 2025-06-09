package com.globalpayments.scale.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String userId;
    private String name;
    private String email;
    private String password;
    private UserRole role;
    private String department;
    private String language;
    private String profilePicture;
    private Boolean aiAssistantEnabled;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum UserRole {
        SUBMITTER, EVALUATOR, ADMIN, IDEA_OWNER
    }
}