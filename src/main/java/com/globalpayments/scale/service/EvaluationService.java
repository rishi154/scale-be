package com.globalpayments.scale.service;


import com.globalpayments.scale.dao.AiAuditLogDao;
import com.globalpayments.scale.dao.EvaluationDao;
import com.globalpayments.scale.dao.IdeaDao;
import com.globalpayments.scale.dao.UserDao;
import com.globalpayments.scale.dto.CreateEvaluationRequest;
import com.globalpayments.scale.dto.EvaluationDto;
import com.globalpayments.scale.exception.ResourceNotFoundException;
import com.globalpayments.scale.model.AiAuditLog;
import com.globalpayments.scale.model.Evaluation;
import com.globalpayments.scale.model.Idea;
import com.globalpayments.scale.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EvaluationService {

    private final EvaluationDao evaluationDao;
    private final UserDao userDao;
    private final IdeaDao ideaDao;
    private final AiAuditLogDao aiAuditLogDao;

    @Autowired
    public EvaluationService(
            EvaluationDao evaluationDao,
            UserDao userDao,
            IdeaDao ideaDao,
            AiAuditLogDao aiAuditLogDao) {
        this.evaluationDao = evaluationDao;
        this.userDao = userDao;
        this.ideaDao = ideaDao;
        this.aiAuditLogDao = aiAuditLogDao;
    }

    public EvaluationDto createEvaluation(CreateEvaluationRequest request, String reviewerId) {
        User reviewer = userDao.findById(reviewerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + reviewerId));

        Idea idea = ideaDao.findById(request.getIdeaId())
                .orElseThrow(() -> new ResourceNotFoundException("Idea not found with ID: " + request.getIdeaId()));

        // Update idea status to UNDER_REVIEW if it's not already further along the process
        if (idea.getStatus() == Idea.IdeaStatus.SUBMITTED ||
                idea.getStatus() == Idea.IdeaStatus.DRAFT) {
            ideaDao.updateStatus(idea.getIdeaId(), Idea.IdeaStatus.UNDER_REVIEW);
        }

        Evaluation evaluation = new Evaluation();
        evaluation.setIdeaId(request.getIdeaId());
        evaluation.setReviewerId(reviewerId);
        evaluation.setDate(LocalDateTime.now());
        evaluation.setRoiScore(request.getRoiScore());
        evaluation.setFeasibilityScore(request.getFeasibilityScore());
        evaluation.setAlignmentScore(request.getAlignmentScore());
        evaluation.setRiskScore(request.getRiskScore());
        evaluation.setImplementationCost(request.getImplementationCost());
        evaluation.setTotalScore(request.getTotalScore());
        evaluation.setReviewComments(request.getReviewComments());

        // If AI assistant is enabled for reviewer, suggest a score
        if (reviewer.getAiAssistantEnabled()) {
            Integer aiScore = suggestAiScore(idea);
            evaluation.setAiScoreSuggestion(aiScore);

            // Log AI usage
            AiAuditLog log = new AiAuditLog();
            log.setUserId(reviewerId);
            log.setActionType(AiAuditLog.ActionType.SCORE_SUGGESTION);
            log.setInputText("Idea: " + idea.getTitle());
            log.setAiOutput("Suggested Score: " + aiScore);
            aiAuditLogDao.create(log);
        }

        String evaluationId = evaluationDao.create(evaluation);

        return evaluationDao.findById(evaluationId)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Failed to create evaluation"));
    }

    public EvaluationDto getEvaluationById(String evaluationId) {
        Evaluation evaluation = evaluationDao.findById(evaluationId)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluation not found with ID: " + evaluationId));

        return mapToDto(evaluation);
    }

    public List<EvaluationDto> getEvaluationsByIdea(String ideaId) {
        if (!ideaDao.findById(ideaId).isPresent()) {
            throw new ResourceNotFoundException("Idea not found with ID: " + ideaId);
        }

        return evaluationDao.findByIdeaId(ideaId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<EvaluationDto> getEvaluationsByReviewer(String reviewerId) {
        if (!userDao.findById(reviewerId).isPresent()) {
            throw new ResourceNotFoundException("User not found with ID: " + reviewerId);
        }

        return evaluationDao.findByReviewerId(reviewerId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<EvaluationDto> getAllEvaluations() {
        return evaluationDao.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public double getAverageScoreForIdea(String ideaId) {
        if (!ideaDao.findById(ideaId).isPresent()) {
            throw new ResourceNotFoundException("Idea not found with ID: " + ideaId);
        }

        Double average = evaluationDao.getAverageScoreByIdeaId(ideaId);
        return average != null ? average : 0.0;
    }

    public EvaluationDto updateEvaluation(String evaluationId, CreateEvaluationRequest request) {
        Evaluation evaluation = evaluationDao.findById(evaluationId)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluation not found with ID: " + evaluationId));

        // Only update if the idea matches
        if (!evaluation.getIdeaId().equals(request.getIdeaId())) {
            throw new IllegalArgumentException("Cannot change the idea associated with an evaluation");
        }

        if (request.getRoiScore() != null) {
            evaluation.setRoiScore(request.getRoiScore());
        }

        if (request.getFeasibilityScore() != null) {
            evaluation.setFeasibilityScore(request.getFeasibilityScore());
        }

        if (request.getAlignmentScore() != null) {
            evaluation.setAlignmentScore(request.getAlignmentScore());
        }

        if (request.getRiskScore() != null) {
            evaluation.setRiskScore(request.getRiskScore());
        }

        if (request.getImplementationCost() != null) {
            evaluation.setImplementationCost(request.getImplementationCost());
        }

        if (request.getTotalScore() != null) {
            evaluation.setTotalScore(request.getTotalScore());
        }

        if (request.getReviewComments() != null) {
            evaluation.setReviewComments(request.getReviewComments());
        }

        evaluationDao.update(evaluation);

        return mapToDto(evaluation);
    }

    public void deleteEvaluation(String evaluationId) {
        if (!evaluationDao.delete(evaluationId)) {
            throw new ResourceNotFoundException("Evaluation not found with ID: " + evaluationId);
        }
    }

    private Integer suggestAiScore(Idea idea) {
        // In a real implementation, this would call an AI service to analyze the idea
        // Here we're just creating a simple mock score based on the idea's properties
        int baseScore = 50;

        // Add points for longer descriptions, assuming more detail
        if (idea.getDescription() != null) {
            int length = idea.getDescription().length();
            baseScore += Math.min(20, length / 50); // Max 20 points for length
        }

        // Add points for votes, assuming popular ideas are better
        if (idea.getVotesCount() != null) {
            baseScore += Math.min(15, idea.getVotesCount()); // Max 15 points for votes
        }

        // Cap the score at 100
        return Math.min(100, baseScore);
    }

    private EvaluationDto mapToDto(Evaluation evaluation) {
        EvaluationDto dto = new EvaluationDto();
        dto.setEvaluationId(evaluation.getEvaluationId());
        dto.setIdeaId(evaluation.getIdeaId());
        dto.setReviewerId(evaluation.getReviewerId());
        dto.setDate(evaluation.getDate());
        dto.setRoiScore(evaluation.getRoiScore());
        dto.setFeasibilityScore(evaluation.getFeasibilityScore());
        dto.setAlignmentScore(evaluation.getAlignmentScore());
        dto.setRiskScore(evaluation.getRiskScore());
        dto.setImplementationCost(evaluation.getImplementationCost());
        dto.setTotalScore(evaluation.getTotalScore());
        dto.setReviewComments(evaluation.getReviewComments());
        dto.setAiScoreSuggestion(evaluation.getAiScoreSuggestion());
        dto.setCreatedAt(evaluation.getCreatedAt());
        dto.setUpdatedAt(evaluation.getUpdatedAt());

        // Get reviewer's name if available
        userDao.findById(evaluation.getReviewerId())
                .ifPresent(user -> dto.setReviewerName(user.getName()));

        // Get idea title if available
        ideaDao.findById(evaluation.getIdeaId())
                .ifPresent(idea -> dto.setIdeaTitle(idea.getTitle()));

        return dto;
    }
}