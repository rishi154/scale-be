package com.globalpayments.scale.service;


import com.globalpayments.scale.dao.FeedbackDao;
import com.globalpayments.scale.dao.IdeaDao;
import com.globalpayments.scale.dao.UserDao;
import com.globalpayments.scale.dto.CreateFeedbackRequest;
import com.globalpayments.scale.dto.FeedbackDto;
import com.globalpayments.scale.exception.ResourceNotFoundException;
import com.globalpayments.scale.model.Feedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackService {

    private final FeedbackDao feedbackDao;
    private final UserDao userDao;
    private final IdeaDao ideaDao;

    @Autowired
    public FeedbackService(FeedbackDao feedbackDao, UserDao userDao, IdeaDao ideaDao) {
        this.feedbackDao = feedbackDao;
        this.userDao = userDao;
        this.ideaDao = ideaDao;
    }

    public FeedbackDto createFeedback(CreateFeedbackRequest request, String submitterId) {
        if (!userDao.findById(submitterId).isPresent()) {
            throw new ResourceNotFoundException("User not found with ID: " + submitterId);
        }

        if (!ideaDao.findById(request.getIdeaId()).isPresent()) {
            throw new ResourceNotFoundException("Idea not found with ID: " + request.getIdeaId());
        }

        Feedback feedback = new Feedback();
        feedback.setIdeaId(request.getIdeaId());
        feedback.setSubmitterId(submitterId);
        feedback.setFeedbackText(request.getFeedbackText());
        feedback.setType(request.getType());
        feedback.setVisibility(request.getVisibility());
        feedback.setRecognitionPoints(0); // Initial points
        feedback.setBadgesAwarded(request.getBadgesAwarded());

        String feedbackId = feedbackDao.create(feedback);

        return feedbackDao.findById(feedbackId)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Failed to create feedback"));
    }

    public FeedbackDto getFeedbackById(String feedbackId) {
        Feedback feedback = feedbackDao.findById(feedbackId)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with ID: " + feedbackId));

        return mapToDto(feedback);
    }

    public List<FeedbackDto> getFeedbackByIdea(String ideaId) {
        if (!ideaDao.findById(ideaId).isPresent()) {
            throw new ResourceNotFoundException("Idea not found with ID: " + ideaId);
        }

        return feedbackDao.findByIdeaId(ideaId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<FeedbackDto> getFeedbackBySubmitter(String submitterId) {
        if (!userDao.findById(submitterId).isPresent()) {
            throw new ResourceNotFoundException("User not found with ID: " + submitterId);
        }

        return feedbackDao.findBySubmitterId(submitterId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<FeedbackDto> getFeedbackByType(Feedback.FeedbackType type) {
        return feedbackDao.findByType(type).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<FeedbackDto> getAllFeedback() {
        return feedbackDao.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public FeedbackDto updateFeedback(String feedbackId, CreateFeedbackRequest request) {
        Feedback feedback = feedbackDao.findById(feedbackId)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with ID: " + feedbackId));

        // Cannot change the idea the feedback is for
        if (!feedback.getIdeaId().equals(request.getIdeaId())) {
            throw new IllegalArgumentException("Cannot change the idea associated with feedback");
        }

        if (request.getFeedbackText() != null) {
            feedback.setFeedbackText(request.getFeedbackText());
        }

        if (request.getType() != null) {
            feedback.setType(request.getType());
        }

        if (request.getVisibility() != null) {
            feedback.setVisibility(request.getVisibility());
        }

        if (request.getBadgesAwarded() != null) {
            feedback.setBadgesAwarded(request.getBadgesAwarded());
        }

        feedbackDao.update(feedback);

        return mapToDto(feedback);
    }

    public FeedbackDto addRecognitionPoints(String feedbackId, int points) {
        Feedback feedback = feedbackDao.findById(feedbackId)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with ID: " + feedbackId));

        int newPoints = feedbackDao.addRecognitionPoints(feedbackId, points);
        feedback.setRecognitionPoints(newPoints);

        return mapToDto(feedback);
    }

    public void deleteFeedback(String feedbackId) {
        if (!feedbackDao.delete(feedbackId)) {
            throw new ResourceNotFoundException("Feedback not found with ID: " + feedbackId);
        }
    }

    public int countFeedbackForIdea(String ideaId) {
        if (!ideaDao.findById(ideaId).isPresent()) {
            throw new ResourceNotFoundException("Idea not found with ID: " + ideaId);
        }

        return feedbackDao.countFeedbacksByIdeaId(ideaId);
    }

    private FeedbackDto mapToDto(Feedback feedback) {
        FeedbackDto dto = new FeedbackDto();
        dto.setFeedbackId(feedback.getFeedbackId());
        dto.setIdeaId(feedback.getIdeaId());
        dto.setSubmitterId(feedback.getSubmitterId());
        dto.setFeedbackText(feedback.getFeedbackText());
        dto.setType(feedback.getType());
        dto.setVisibility(feedback.getVisibility());
        dto.setRecognitionPoints(feedback.getRecognitionPoints());
        dto.setBadgesAwarded(feedback.getBadgesAwarded());
        dto.setCreatedAt(feedback.getCreatedAt());
        dto.setUpdatedAt(feedback.getUpdatedAt());

        // Get submitter's name if available
        userDao.findById(feedback.getSubmitterId())
                .ifPresent(user -> dto.setSubmitterName(user.getName()));

        // Get idea title if available
        ideaDao.findById(feedback.getIdeaId())
                .ifPresent(idea -> dto.setIdeaTitle(idea.getTitle()));

        return dto;
    }
}