package com.globalpayments.scale.controller;

import com.globalpayments.scale.dto.CreateFeedbackRequest;
import com.globalpayments.scale.dto.FeedbackDto;
import com.globalpayments.scale.model.Feedback;
import com.globalpayments.scale.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public ResponseEntity<FeedbackDto> createFeedback(@Valid @RequestBody CreateFeedbackRequest request,
                                                      @RequestParam String submitterId) {
        return new ResponseEntity<>(feedbackService.createFeedback(request, submitterId), HttpStatus.CREATED);
    }

    @PostMapping("/ideas/{ideaId}")
    public ResponseEntity<FeedbackDto> createFeedbackForIdea(@PathVariable String ideaId, @Valid @RequestBody CreateFeedbackRequest request,
                                                      @RequestParam String submitterId) {
        return new ResponseEntity<>(feedbackService.createFeedback(request, submitterId), HttpStatus.CREATED);
    }
    @GetMapping("/{feedbackId}")
    public ResponseEntity<FeedbackDto> getFeedbackById(@PathVariable String feedbackId) {
        return ResponseEntity.ok(feedbackService.getFeedbackById(feedbackId));
    }

    @GetMapping("/idea/{ideaId}")
    public ResponseEntity<List<FeedbackDto>> getFeedbackByIdea(@PathVariable String ideaId) {
        return ResponseEntity.ok(feedbackService.getFeedbackByIdea(ideaId));
    }

    @GetMapping("/submitter/{submitterId}")
    public ResponseEntity<List<FeedbackDto>> getFeedbackBySubmitter(@PathVariable String submitterId) {
        return ResponseEntity.ok(feedbackService.getFeedbackBySubmitter(submitterId));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<FeedbackDto>> getFeedbackByType(@PathVariable Feedback.FeedbackType type) {
        return ResponseEntity.ok(feedbackService.getFeedbackByType(type));
    }

    @GetMapping
    public ResponseEntity<List<FeedbackDto>> getAllFeedback() {
        return ResponseEntity.ok(feedbackService.getAllFeedback());
    }

    @PutMapping("/{feedbackId}")
    public ResponseEntity<FeedbackDto> updateFeedback(@PathVariable String feedbackId,
                                                      @Valid @RequestBody CreateFeedbackRequest request) {
        return ResponseEntity.ok(feedbackService.updateFeedback(feedbackId, request));
    }

    @PutMapping("/{feedbackId}/points")
    public ResponseEntity<FeedbackDto> addRecognitionPoints(@PathVariable String feedbackId,
                                                            @RequestParam int points) {
        return ResponseEntity.ok(feedbackService.addRecognitionPoints(feedbackId, points));
    }

    @DeleteMapping("/{feedbackId}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable String feedbackId) {
        feedbackService.deleteFeedback(feedbackId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/idea/{ideaId}/count")
    public ResponseEntity<Integer> countFeedbackForIdea(@PathVariable String ideaId) {
        return ResponseEntity.ok(feedbackService.countFeedbackForIdea(ideaId));
    }
}