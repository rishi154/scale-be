package com.globalpayments.scale.controller;

import com.globalpayments.scale.dto.AiAuditLogDto;
import com.globalpayments.scale.model.AiAuditLog;
import com.globalpayments.scale.service.AiAuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/ai-logs")
public class AiAuditLogController {

    private final AiAuditLogService aiAuditLogService;

    @Autowired
    public AiAuditLogController(AiAuditLogService aiAuditLogService) {
        this.aiAuditLogService = aiAuditLogService;
    }

    @PostMapping
    public ResponseEntity<AiAuditLogDto> createAuditLog(@RequestParam String userId,
                                                        @RequestParam AiAuditLog.ActionType actionType,
                                                        @RequestParam String inputText,
                                                        @RequestParam String aiOutput) {
        return new ResponseEntity<>(
                aiAuditLogService.createAuditLog(userId, actionType, inputText, aiOutput),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{actionId}")
    public ResponseEntity<AiAuditLogDto> getAuditLogById(@PathVariable String actionId) {
        return ResponseEntity.ok(aiAuditLogService.getAuditLogById(actionId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AiAuditLogDto>> getAuditLogsByUser(@PathVariable String userId) {
        return ResponseEntity.ok(aiAuditLogService.getAuditLogsByUser(userId));
    }

    @GetMapping("/type/{actionType}")
    public ResponseEntity<List<AiAuditLogDto>> getAuditLogsByType(@PathVariable AiAuditLog.ActionType actionType) {
        return ResponseEntity.ok(aiAuditLogService.getAuditLogsByType(actionType));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<AiAuditLogDto>> getAuditLogsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(aiAuditLogService.getAuditLogsByDateRange(start, end));
    }

    @GetMapping
    public ResponseEntity<List<AiAuditLogDto>> getAllAuditLogs() {
        return ResponseEntity.ok(aiAuditLogService.getAllAuditLogs());
    }
}
