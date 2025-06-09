package com.globalpayments.scale.dao;

import com.globalpayments.scale.model.AiAuditLog;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AiAuditLogDao {
    String create(AiAuditLog auditLog);
    Optional<AiAuditLog> findById(String actionId);
    List<AiAuditLog> findByUserId(String userId);
    List<AiAuditLog> findByActionType(AiAuditLog.ActionType actionType);
    List<AiAuditLog> findByDateRange(LocalDateTime start, LocalDateTime end);
    List<AiAuditLog> findAll();
}