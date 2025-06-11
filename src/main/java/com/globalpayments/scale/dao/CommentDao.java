package com.globalpayments.scale.dao;

import com.globalpayments.scale.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentDao {
    String create(Comment comment);

    Optional<Comment> findById(String commentId);

    List<Comment> findByIdeaId(String ideaId);

    List<Comment> findBySubmitterId(String submitterId);

    List<Comment> findByType(Comment.CommentType type);

    List<Comment> findAll();

    void update(Comment comment);

    boolean delete(String commentId);

    int addRecognitionPoints(String commentId, int points);

    int countCommentsByIdeaId(String ideaId);
}