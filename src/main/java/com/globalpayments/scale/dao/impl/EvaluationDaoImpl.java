package com.globalpayments.scale.dao.impl;

import com.globalpayments.scale.dao.EvaluationDao;
import com.globalpayments.scale.model.Evaluation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class EvaluationDaoImpl implements EvaluationDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EvaluationDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<Evaluation> EVALUATION_ROW_MAPPER = (rs, rowNum) -> {
        Evaluation evaluation = new Evaluation();
        evaluation.setEvaluationId(rs.getString("evaluation_id"));
        evaluation.setIdeaId(rs.getString("idea_id"));
        evaluation.setReviewerId(rs.getString("reviewer_id"));
        evaluation.setDate(rs.getTimestamp("date").toLocalDateTime());
        evaluation.setRoiScore(rs.getInt("roi_score"));
        evaluation.setFeasibilityScore(rs.getInt("feasibility_score"));
        evaluation.setAlignmentScore(rs.getInt("alignment_score"));
        evaluation.setRiskScore(rs.getInt("risk_score"));
        evaluation.setImplementationCost(rs.getBigDecimal("implementation_cost"));
        evaluation.setTotalScore(rs.getInt("total_score"));
        evaluation.setReviewComments(rs.getString("review_comments"));
        evaluation.setAiScoreSuggestion(rs.getInt("ai_score_suggestion"));
        evaluation.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        evaluation.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return evaluation;
    };

    @Override
    public String create(Evaluation evaluation) {
        String evaluationId = UUID.randomUUID().toString();

        String sql = "INSERT INTO evaluations (evaluation_id, idea_id, reviewer_id, roi_score, " +
                "feasibility_score, alignment_score, risk_score, implementation_cost, " +
                "total_score, review_comments, ai_score_suggestion) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                evaluationId,
                evaluation.getIdeaId(),
                evaluation.getReviewerId(),
                evaluation.getRoiScore(),
                evaluation.getFeasibilityScore(),
                evaluation.getAlignmentScore(),
                evaluation.getRiskScore(),
                evaluation.getImplementationCost(),
                evaluation.getTotalScore(),
                evaluation.getReviewComments(),
                evaluation.getAiScoreSuggestion());

        return evaluationId;
    }

    @Override
    public Optional<Evaluation> findById(String evaluationId) {
        try {
            String sql = "SELECT * FROM evaluations WHERE evaluation_id = ?";
            Evaluation evaluation = jdbcTemplate.queryForObject(sql, EVALUATION_ROW_MAPPER, evaluationId);
            return Optional.ofNullable(evaluation);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Evaluation> findByIdeaId(String ideaId) {
        String sql = "SELECT * FROM evaluations WHERE idea_id = ? ORDER BY date DESC";
        return jdbcTemplate.query(sql, EVALUATION_ROW_MAPPER, ideaId);
    }

    @Override
    public List<Evaluation> findByReviewerId(String reviewerId) {
        String sql = "SELECT * FROM evaluations WHERE reviewer_id = ? ORDER BY date DESC";
        return jdbcTemplate.query(sql, EVALUATION_ROW_MAPPER, reviewerId);
    }

    @Override
    public Double getAverageScoreByIdeaId(String ideaId) {
        String sql = "SELECT AVG(total_score) FROM evaluations WHERE idea_id = ?";
        return jdbcTemplate.queryForObject(sql, Double.class, ideaId);
    }

    @Override
    public List<Evaluation> findAll() {
        String sql = "SELECT * FROM evaluations ORDER BY date DESC";
        return jdbcTemplate.query(sql, EVALUATION_ROW_MAPPER);
    }

    @Override
    public void update(Evaluation evaluation) {
        String sql = "UPDATE evaluations SET roi_score = ?, feasibility_score = ?, " +
                "alignment_score = ?, risk_score = ?, implementation_cost = ?, " +
                "total_score = ?, review_comments = ?, ai_score_suggestion = ? " +
                "WHERE evaluation_id = ?";

        jdbcTemplate.update(sql,
                evaluation.getRoiScore(),
                evaluation.getFeasibilityScore(),
                evaluation.getAlignmentScore(),
                evaluation.getRiskScore(),
                evaluation.getImplementationCost(),
                evaluation.getTotalScore(),
                evaluation.getReviewComments(),
                evaluation.getAiScoreSuggestion(),
                evaluation.getEvaluationId());
    }

    @Override
    public boolean delete(String evaluationId) {
        String sql = "DELETE FROM evaluations WHERE evaluation_id = ?";
        return jdbcTemplate.update(sql, evaluationId) > 0;
    }
}