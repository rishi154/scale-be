package com.globalpayments.scale.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiAuditLog {
    private String actionId;
    private String userId;
    private ActionType actionType;
    private String inputText;
    private String aiOutput;
    private LocalDateTime timestamp;

    public enum ActionType {
        SUMMARY, CLASSIFICATION, SCORE_SUGGESTION, LANGUAGE_TRANSLATION
    }
}