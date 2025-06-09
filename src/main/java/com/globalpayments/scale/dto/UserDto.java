package com.globalpayments.scale.dto;

import com.globalpayments.scale.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String userId;
    private String name;
    private String email;
    private User.UserRole role;
    private String department;
    private String language;
    private String profilePicture;
    private Boolean aiAssistantEnabled;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}