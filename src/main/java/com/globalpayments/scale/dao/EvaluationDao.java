package com.globalpayments.scale.dao;

import com.globalpayments.scale.model.Evaluation;

import java.util.List;
import java.util.Optional;

public interface EvaluationDao {
    String create(Evaluation evaluation);
    Optional<Evaluation> findById(String evaluationId);
    List<Evaluation> findByIdeaId(String ideaId);
    List<Evaluation> findByReviewerId(String reviewerId);
    Double getAverageScoreByIdeaId(String ideaId);
    List<Evaluation> findAll();
    void update(Evaluation evaluation);
    boolean delete(String evaluationId);
}