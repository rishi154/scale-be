package com.globalpayments.scale.service;


import com.globalpayments.scale.dao.UserDao;
import com.globalpayments.scale.dto.CreateUserRequest;
import com.globalpayments.scale.dto.UpdateUserRequest;
import com.globalpayments.scale.dto.UserDto;
import com.globalpayments.scale.exception.ResourceNotFoundException;
import com.globalpayments.scale.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto createUser(CreateUserRequest request) {
        if (userDao.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setDepartment(request.getDepartment());
        user.setLanguage(request.getLanguage());
        user.setProfilePicture(request.getProfilePicture());
        user.setAiAssistantEnabled(request.getAiAssistantEnabled());
        user.setActive(true);

        String userId = userDao.create(user);

        return userDao.findById(userId)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Failed to create user"));
    }

    public UserDto getUserById(String userId) {
        return userDao.findById(userId)
                .map(this::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    }

    public UserDto getUserByEmail(String email) {
        return userDao.findByEmail(email)
                .map(this::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    public List<UserDto> getAllUsers() {
        return userDao.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public UserDto updateUser(String userId, UpdateUserRequest request) {
        User user = userDao.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        if (request.getName() != null) {
            user.setName(request.getName());
        }

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            // Check if email is already in use by another user
            if (userDao.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Email already in use");
            }
            user.setEmail(request.getEmail());
        }

        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }

        if (request.getDepartment() != null) {
            user.setDepartment(request.getDepartment());
        }

        if (request.getLanguage() != null) {
            user.setLanguage(request.getLanguage());
        }

        if (request.getProfilePicture() != null) {
            user.setProfilePicture(request.getProfilePicture());
        }

        if (request.getAiAssistantEnabled() != null) {
            user.setAiAssistantEnabled(request.getAiAssistantEnabled());
        }

        if (request.getActive() != null) {
            user.setActive(request.getActive());
        }

        userDao.update(user);

        return mapToDto(user);
    }

    public void changePassword(String userId, String newPassword) {
        User user = userDao.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        userDao.updatePassword(userId, passwordEncoder.encode(newPassword));
    }

    public void deleteUser(String userId) {
        if (!userDao.delete(userId)) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }
    }

    private UserDto mapToDto(User user) {
        UserDto dto = new UserDto();
        dto.setUserId(user.getUserId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setDepartment(user.getDepartment());
        dto.setLanguage(user.getLanguage());
        dto.setProfilePicture(user.getProfilePicture());
        dto.setAiAssistantEnabled(user.getAiAssistantEnabled());
        dto.setActive(user.getActive());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
}