package com.globalpayments.scale.dao;

import com.globalpayments.scale.model.Feedback;

import java.util.List;
import java.util.Optional;

public interface FeedbackDao {
    String create(Feedback feedback);
    Optional<Feedback> findById(String feedbackId);
    List<Feedback> findByIdeaId(String ideaId);
    List<Feedback> findBySubmitterId(String submitterId);
    List<Feedback> findByType(Feedback.FeedbackType type);
    List<Feedback> findAll();
    void update(Feedback feedback);
    boolean delete(String feedbackId);
    int addRecognitionPoints(String feedbackId, int points);
    int countFeedbacksByIdeaId(String ideaId);
}