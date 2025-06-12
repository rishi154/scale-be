package com.globalpayments.scale.dao.impl;

import com.globalpayments.scale.dao.CommentDao;
import com.globalpayments.scale.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CommentDaoImpl implements CommentDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CommentDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<Comment> COMMENT_ROW_MAPPER = (rs, rowNum) -> {
        Comment comment = new Comment();
        comment.setCommentId(rs.getString("comment_id"));
        comment.setIdeaId(rs.getString("idea_id"));
        comment.setSubmitterId(rs.getString("submitter_id"));
        comment.setCommentText(rs.getString("comment_text"));
        comment.setType(Comment.CommentType.valueOf(rs.getString("type")));
        comment.setVisibility(Comment.Visibility.valueOf(rs.getString("visibility")));
        comment.setRecognitionPoints(rs.getInt("recognition_points"));
        comment.setBadgesAwarded(rs.getString("badges_awarded"));
        comment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        comment.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return comment;
    };

    @Override
    public String create(Comment comment) {
        String commentId = UUID.randomUUID().toString();
        String sql = "INSERT INTO comment (comment_id, idea_id, submitter_id, " + "comment_text, type, visibility, recognition_points, badges_awarded) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, commentId, comment.getIdeaId(), comment.getSubmitterId(), comment.getCommentText(), comment.getType().toString(), comment.getVisibility().toString(), comment.getRecognitionPoints(), comment.getBadgesAwarded());
        return commentId;
    }

    @Override
    public Optional<Comment> findById(String commentId) {
        try {
            String sql = "SELECT * FROM comment WHERE comment_id = ?";
            Comment comment = jdbcTemplate.queryForObject(sql, COMMENT_ROW_MAPPER, commentId);
            return Optional.ofNullable(comment);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Comment> findByIdeaId(String ideaId) {
        String sql = "SELECT * FROM comment WHERE idea_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, COMMENT_ROW_MAPPER, ideaId);
    }

    @Override
    public List<Comment> findBySubmitterId(String submitterId) {
        String sql = "SELECT * FROM comment WHERE submitter_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, COMMENT_ROW_MAPPER, submitterId);
    }

    @Override
    public List<Comment> findByType(Comment.CommentType type) {
        String sql = "SELECT * FROM comment WHERE type = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, COMMENT_ROW_MAPPER, type.toString());
    }

    @Override
    public List<Comment> findAll() {
        String sql = "SELECT * FROM comment ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, COMMENT_ROW_MAPPER);
    }

    @Override
    public void update(Comment comment) {
        String sql = "UPDATE comment SET comment_text = ?, type = ?, " + "visibility = ?, recognition_points = ?, badges_awarded = ? " + "WHERE comment_id = ?";
        jdbcTemplate.update(sql, comment.getCommentText(), comment.getType().toString(), comment.getVisibility().toString(), comment.getRecognitionPoints(), comment.getBadgesAwarded(), comment.getCommentId());
    }

    @Override
    public boolean delete(String commentId) {
        String sql = "DELETE FROM comment WHERE comment_id = ?";
        return jdbcTemplate.update(sql, commentId) > 0;
    }

    @Override
    public int addRecognitionPoints(String commentId, int points) {
        String sql = "UPDATE comment SET recognition_points = recognition_points + ? " +
                "WHERE comment_id = ?";
        jdbcTemplate.update(sql, points, commentId);

        // Return the updated points
        String getPointsSql = "SELECT recognition_points FROM comment WHERE comment_id = ?";
        return jdbcTemplate.queryForObject(getPointsSql, Integer.class, commentId);
    }

    @Override
    public int countCommentsByIdeaId(String ideaId) {
        String sql = "SELECT COUNT(*) FROM comment WHERE idea_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, ideaId);
    }
}