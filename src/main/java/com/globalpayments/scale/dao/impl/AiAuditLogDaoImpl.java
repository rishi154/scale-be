package com.globalpayments.scale.dao.impl;

import com.globalpayments.scale.dao.AiAuditLogDao;
import com.globalpayments.scale.model.AiAuditLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AiAuditLogDaoImpl implements AiAuditLogDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AiAuditLogDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<AiAuditLog> AI_AUDIT_LOG_ROW_MAPPER = (rs, rowNum) -> {
        AiAuditLog log = new AiAuditLog();
        log.setActionId(rs.getString("action_id"));
        log.setUserId(rs.getString("user_id"));
        log.setActionType(AiAuditLog.ActionType.valueOf(rs.getString("action_type")));
        log.setInputText(rs.getString("input_text"));
        log.setAiOutput(rs.getString("ai_output"));
        log.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
        return log;
    };

    @Override
    public String create(AiAuditLog auditLog) {
        String actionId = UUID.randomUUID().toString();

        String sql = "INSERT INTO ai_audit_logs (action_id, user_id, action_type, " +
                "input_text, ai_output) VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                actionId,
                auditLog.getUserId(),
                auditLog.getActionType().toString(),
                auditLog.getInputText(),
                auditLog.getAiOutput());

        return actionId;
    }

    @Override
    public Optional<AiAuditLog> findById(String actionId) {
        try {
            String sql = "SELECT * FROM ai_audit_logs WHERE action_id = ?";
            AiAuditLog log = jdbcTemplate.queryForObject(sql, AI_AUDIT_LOG_ROW_MAPPER, actionId);
            return Optional.ofNullable(log);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<AiAuditLog> findByUserId(String userId) {
        String sql = "SELECT * FROM ai_audit_logs WHERE user_id = ? ORDER BY timestamp DESC";
        return jdbcTemplate.query(sql, AI_AUDIT_LOG_ROW_MAPPER, userId);
    }

    @Override
    public List<AiAuditLog> findByActionType(AiAuditLog.ActionType actionType) {
        String sql = "SELECT * FROM ai_audit_logs WHERE action_type = ? ORDER BY timestamp DESC";
        return jdbcTemplate.query(sql, AI_AUDIT_LOG_ROW_MAPPER, actionType.toString());
    }

    @Override
    public List<AiAuditLog> findByDateRange(LocalDateTime start, LocalDateTime end) {
        String sql = "SELECT * FROM ai_audit_logs WHERE timestamp BETWEEN ? AND ? ORDER BY timestamp DESC";
        return jdbcTemplate.query(sql, AI_AUDIT_LOG_ROW_MAPPER,
                java.sql.Timestamp.valueOf(start),
                java.sql.Timestamp.valueOf(end));
    }

    @Override
    public List<AiAuditLog> findAll() {
        String sql = "SELECT * FROM ai_audit_logs ORDER BY timestamp DESC";
        return jdbcTemplate.query(sql, AI_AUDIT_LOG_ROW_MAPPER);
    }
}
