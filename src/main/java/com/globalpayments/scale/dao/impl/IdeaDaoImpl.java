package com.globalpayments.scale.dao.impl;

import com.globalpayments.scale.dao.IdeaDao;
import com.globalpayments.scale.model.Idea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class IdeaDaoImpl implements IdeaDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public IdeaDaoImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private static final RowMapper<Idea> IDEA_ROW_MAPPER = (ResultSet rs, int rowNum) -> {
        Idea idea = new Idea();
        idea.setIdeaId(rs.getString("idea_id"));
        idea.setTitle(rs.getString("title"));
        idea.setDescription(rs.getString("description"));
        idea.setSubmittedBy(rs.getString("submitted_by"));
        idea.setSubmissionDate(rs.getTimestamp("submission_date").toLocalDateTime());
        idea.setDepartment(rs.getString("department"));
        idea.setTags(rs.getString("tags"));
        idea.setAttachments(rs.getString("attachments"));
        idea.setVotesCount(rs.getInt("votes_count"));
        idea.setViewCount(rs.getInt("view_count"));
        idea.setStatus(Idea.IdeaStatus.valueOf(rs.getString("status")));
        idea.setStage(Idea.IdeaStage.valueOf(rs.getString("stage")));
        idea.setLanguage(rs.getString("language"));
        idea.setAiSummary(rs.getString("ai_summary"));
        idea.setAiClusterId(rs.getString("ai_cluster_id"));
        idea.setCampaignId(rs.getString("campaign_id"));
        idea.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        idea.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return idea;
    };

    @Override
    public String create(Idea idea) {
        String ideaId = UUID.randomUUID().toString();

        String sql = "INSERT INTO ideas (idea_id, title, description, submitted_by, department, " +
                "tags, attachments, status, stage, language, ai_summary, ai_cluster_id, campaign_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                ideaId,
                idea.getTitle(),
                idea.getDescription(),
                idea.getSubmittedBy(),
                idea.getDepartment(),
                idea.getTags(),
                idea.getAttachments(),
                idea.getStatus().toString(),
                idea.getStage().toString(),
                idea.getLanguage(),
                idea.getAiSummary(),
                idea.getAiClusterId(),
                idea.getCampaignId());

        return ideaId;
    }

    @Override
    public Optional<Idea> findById(String ideaId) {
        try {
            String sql = "SELECT * FROM ideas WHERE idea_id = ?";
            Idea idea = jdbcTemplate.queryForObject(sql, IDEA_ROW_MAPPER, ideaId);
            return Optional.ofNullable(idea);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Idea> findBySubmitterId(String submitterId) {
        String sql = "SELECT * FROM ideas WHERE submitted_by = ? ORDER BY submission_date DESC";
        return jdbcTemplate.query(sql, IDEA_ROW_MAPPER, submitterId);
    }

    @Override
    public List<Idea> findByCampaignId(String campaignId) {
        String sql = "SELECT * FROM ideas WHERE campaign_id = ? ORDER BY votes_count DESC, submission_date DESC";
        return jdbcTemplate.query(sql, IDEA_ROW_MAPPER, campaignId);
    }

    @Override
    public List<Idea> findByStatus(Idea.IdeaStatus status) {
        String sql = "SELECT * FROM ideas WHERE status = ? ORDER BY submission_date DESC";
        return jdbcTemplate.query(sql, IDEA_ROW_MAPPER, status.toString());
    }

    @Override
    public List<Idea> findByStage(Idea.IdeaStage stage) {
        String sql = "SELECT * FROM ideas WHERE stage = ? ORDER BY submission_date DESC";
        return jdbcTemplate.query(sql, IDEA_ROW_MAPPER, stage.toString());
    }

    @Override
    public List<Idea> findAll() {
        String sql = "SELECT * FROM ideas ORDER BY submission_date DESC";
        return jdbcTemplate.query(sql, IDEA_ROW_MAPPER);
    }

    @Override
    public List<Idea> findTopVotedIdeas(int limit) {
        String sql = "SELECT * FROM ideas ORDER BY votes_count DESC, view_count DESC LIMIT ?";
        return jdbcTemplate.query(sql, IDEA_ROW_MAPPER, limit);
    }

    @Override
    public void update(Idea idea) {
        String sql = "UPDATE ideas SET title = ?, description = ?, department = ?, " +
                "tags = ?, attachments = ?, status = ?, stage = ?, language = ?, " +
                "ai_summary = ?, ai_cluster_id = ?, campaign_id = ? " +
                "WHERE idea_id = ?";

        jdbcTemplate.update(sql,
                idea.getTitle(),
                idea.getDescription(),
                idea.getDepartment(),
                idea.getTags(),
                idea.getAttachments(),
                idea.getStatus().toString(),
                idea.getStage().toString(),
                idea.getLanguage(),
                idea.getAiSummary(),
                idea.getAiClusterId(),
                idea.getCampaignId(),
                idea.getIdeaId());
    }

    @Override
    public boolean updateStatus(String ideaId, Idea.IdeaStatus status) {
        String sql = "UPDATE ideas SET status = ? WHERE idea_id = ?";
        return jdbcTemplate.update(sql, status.toString(), ideaId) > 0;
    }

    @Override
    public boolean updateStage(String ideaId, Idea.IdeaStage stage) {
        String sql = "UPDATE ideas SET stage = ? WHERE idea_id = ?";
        return jdbcTemplate.update(sql, stage.toString(), ideaId) > 0;
    }

    @Override
    public boolean delete(String ideaId) {
        String sql = "DELETE FROM ideas WHERE idea_id = ?";
        return jdbcTemplate.update(sql, ideaId) > 0;
    }

    @Override
    public void incrementViewCount(String ideaId) {
        String sql = "UPDATE ideas SET view_count = view_count + 1 WHERE idea_id = ?";
        jdbcTemplate.update(sql, ideaId);
    }

    @Override
    public void incrementVoteCount(String ideaId) {
        String sql = "UPDATE ideas SET votes_count = votes_count + 1 WHERE idea_id = ?";
        jdbcTemplate.update(sql, ideaId);
    }

    @Override
    public void decrementVoteCount(String ideaId) {
        String sql = "UPDATE ideas SET votes_count = GREATEST(votes_count - 1, 0) WHERE idea_id = ?";
        jdbcTemplate.update(sql, ideaId);
    }
}