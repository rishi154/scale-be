package com.globalpayments.scale.service;

import com.globalpayments.scale.dao.AiAuditLogDao;
import com.globalpayments.scale.dao.UserDao;
import com.globalpayments.scale.dto.AiAuditLogDto;
import com.globalpayments.scale.exception.ResourceNotFoundException;
import com.globalpayments.scale.model.AiAuditLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AiAuditLogService {

    private final AiAuditLogDao aiAuditLogDao;
    private final UserDao userDao;

    @Autowired
    public AiAuditLogService(AiAuditLogDao aiAuditLogDao, UserDao userDao) {
        this.aiAuditLogDao = aiAuditLogDao;
        this.userDao = userDao;
    }

    public AiAuditLogDto createAuditLog(String userId, AiAuditLog.ActionType actionType, String inputText, String aiOutput) {
        if (!userDao.findById(userId).isPresent()) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }

        AiAuditLog log = new AiAuditLog();
        log.setUserId(userId);
        log.setActionType(actionType);
        log.setInputText(inputText);
        log.setAiOutput(aiOutput);

        String actionId = aiAuditLogDao.create(log);

        return aiAuditLogDao.findById(actionId)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Failed to create AI audit log"));
    }

    public AiAuditLogDto getAuditLogById(String actionId) {
        AiAuditLog log = aiAuditLogDao.findById(actionId)
                .orElseThrow(() -> new ResourceNotFoundException("AI audit log not found with ID: " + actionId));

        return mapToDto(log);
    }

    public List<AiAuditLogDto> getAuditLogsByUser(String userId) {
        if (!userDao.findById(userId).isPresent()) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }

        return aiAuditLogDao.findByUserId(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<AiAuditLogDto> getAuditLogsByType(AiAuditLog.ActionType actionType) {
        return aiAuditLogDao.findByActionType(actionType).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<AiAuditLogDto> getAuditLogsByDateRange(LocalDateTime start, LocalDateTime end) {
        return aiAuditLogDao.findByDateRange(start, end).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<AiAuditLogDto> getAllAuditLogs() {
        return aiAuditLogDao.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private AiAuditLogDto mapToDto(AiAuditLog log) {
        AiAuditLogDto dto = new AiAuditLogDto();
        dto.setActionId(log.getActionId());
        dto.setUserId(log.getUserId());
        dto.setActionType(log.getActionType());
        dto.setInputText(log.getInputText());
        dto.setAiOutput(log.getAiOutput());
        dto.setTimestamp(log.getTimestamp());

        // Get user's name if available
        userDao.findById(log.getUserId())
                .ifPresent(user -> dto.setUserName(user.getName()));

        return dto;
    }
}