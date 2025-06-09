package com.globalpayments.scale.dao.impl;

import com.globalpayments.scale.dao.FeedbackDao;
import com.globalpayments.scale.model.Feedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FeedbackDaoImpl implements FeedbackDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FeedbackDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<Feedback> FEEDBACK_ROW_MAPPER = (rs, rowNum) -> {
        Feedback feedback = new Feedback();
        feedback.setFeedbackId(rs.getString("feedback_id"));
        feedback.setIdeaId(rs.getString("idea_id"));
        feedback.setSubmitterId(rs.getString("submitter_id"));
        feedback.setFeedbackText(rs.getString("feedback_text"));
        feedback.setType(Feedback.FeedbackType.valueOf(rs.getString("type")));
        feedback.setVisibility(Feedback.Visibility.valueOf(rs.getString("visibility")));
        feedback.setRecognitionPoints(rs.getInt("recognition_points"));
        feedback.setBadgesAwarded(rs.getString("badges_awarded"));
        feedback.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        feedback.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return feedback;
    };

    @Override
    public String create(Feedback feedback) {
        String feedbackId = UUID.randomUUID().toString();

        String sql = "INSERT INTO feedback (feedback_id, idea_id, submitter_id, " +
                "feedback_text, type, visibility, recognition_points, badges_awarded) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                feedbackId,
                feedback.getIdeaId(),
                feedback.getSubmitterId(),
                feedback.getFeedbackText(),
                feedback.getType().toString(),
                feedback.getVisibility().toString(),
                feedback.getRecognitionPoints(),
                feedback.getBadgesAwarded());

        return feedbackId;
    }

    @Override
    public Optional<Feedback> findById(String feedbackId) {
        try {
            String sql = "SELECT * FROM feedback WHERE feedback_id = ?";
            Feedback feedback = jdbcTemplate.queryForObject(sql, FEEDBACK_ROW_MAPPER, feedbackId);
            return Optional.ofNullable(feedback);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Feedback> findByIdeaId(String ideaId) {
        String sql = "SELECT * FROM feedback WHERE idea_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, FEEDBACK_ROW_MAPPER, ideaId);
    }

    @Override
    public List<Feedback> findBySubmitterId(String submitterId) {
        String sql = "SELECT * FROM feedback WHERE submitter_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, FEEDBACK_ROW_MAPPER, submitterId);
    }

    @Override
    public List<Feedback> findByType(Feedback.FeedbackType type) {
        String sql = "SELECT * FROM feedback WHERE type = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, FEEDBACK_ROW_MAPPER, type.toString());
    }

    @Override
    public List<Feedback> findAll() {
        String sql = "SELECT * FROM feedback ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, FEEDBACK_ROW_MAPPER);
    }

    @Override
    public void update(Feedback feedback) {
        String sql = "UPDATE feedback SET feedback_text = ?, type = ?, " +
                "visibility = ?, recognition_points = ?, badges_awarded = ? " +
                "WHERE feedback_id = ?";

        jdbcTemplate.update(sql,
                feedback.getFeedbackText(),
                feedback.getType().toString(),
                feedback.getVisibility().toString(),
                feedback.getRecognitionPoints(),
                feedback.getBadgesAwarded(),
                feedback.getFeedbackId());
    }

    @Override
    public boolean delete(String feedbackId) {
        String sql = "DELETE FROM feedback WHERE feedback_id = ?";
        return jdbcTemplate.update(sql, feedbackId) > 0;
    }

    @Override
    public int addRecognitionPoints(String feedbackId, int points) {
        String sql = "UPDATE feedback SET recognition_points = recognition_points + ? " +
                "WHERE feedback_id = ?";
        jdbcTemplate.update(sql, points, feedbackId);

        // Return the updated points
        String getPointsSql = "SELECT recognition_points FROM feedback WHERE feedback_id = ?";
        return jdbcTemplate.queryForObject(getPointsSql, Integer.class, feedbackId);
    }

    @Override
    public int countFeedbacksByIdeaId(String ideaId) {
        String sql = "SELECT COUNT(*) FROM feedback WHERE idea_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, ideaId);
    }
}
