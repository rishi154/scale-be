package com.globalpayments.scale.dto;

import com.globalpayments.scale.model.AiAuditLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiAuditLogDto {
    private String actionId;
    private String userId;
    private String userName; // Additional field for user's name
    private AiAuditLog.ActionType actionType;
    private String inputText;
    private String aiOutput;
    private LocalDateTime timestamp;
}