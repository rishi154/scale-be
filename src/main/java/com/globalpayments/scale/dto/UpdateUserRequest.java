package com.globalpayments.scale.dto;

import com.globalpayments.scale.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Email(message = "Email should be valid")
    private String email;

    private User.UserRole role;

    private String department;

    private String language;

    private String profilePicture;

    private Boolean aiAssistantEnabled;

    private Boolean active;
}