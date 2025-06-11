package com.globalpayments.scale.controller;
import com.globalpayments.scale.dto.CommentDto;
import com.globalpayments.scale.dto.CreateCommentRequest;
import com.globalpayments.scale.dto.CreateFeedbackRequest;
import com.globalpayments.scale.dto.FeedbackDto;
import com.globalpayments.scale.model.Comment;
import com.globalpayments.scale.model.Feedback;
import com.globalpayments.scale.service.CommentService;
import com.globalpayments.scale.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    @Autowired public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    @PostMapping public ResponseEntity < CommentDto > createComment(@Valid @RequestBody CreateCommentRequest request, @RequestParam String submitterId) {
        return new ResponseEntity < > (commentService.createComment(request, submitterId), HttpStatus.CREATED);
    }
    @GetMapping("/{feedbackId}") public ResponseEntity < CommentDto > getCommentById(@PathVariable String commentId) {
        return ResponseEntity.ok(commentService.getCommentById(commentId));
    }
    @GetMapping("/idea/{ideaId}") public ResponseEntity < List < CommentDto >> getFeedbackByIdea(@PathVariable String ideaId) {
        return ResponseEntity.ok(commentService.getCommentByIdea(ideaId));
    }
    @GetMapping("/submitter/{submitterId}") public ResponseEntity < List < CommentDto >> getFeedbackBySubmitter(@PathVariable String submitterId) {
        return ResponseEntity.ok(commentService.getCommentBySubmitter(submitterId));
    }
    @GetMapping("/type/{type}") public ResponseEntity < List < CommentDto >> getFeedbackByType(@PathVariable Comment.CommentType type) {
        return ResponseEntity.ok(commentService.getCommentByType(type));
    }
    @GetMapping public ResponseEntity < List < CommentDto >> getAllFeedback() {
        return ResponseEntity.ok(commentService.getAllComment());
    }
    @PutMapping("/{feedbackId}") public ResponseEntity < CommentDto > updateFeedback(@PathVariable String commentId, @Valid @RequestBody CreateCommentRequest request) {
        return ResponseEntity.ok(commentService.updateComment(commentId, request));
    }
    @PutMapping("/{feedbackId}/points") public ResponseEntity < CommentDto > addRecognitionPoints(@PathVariable String feedbackId, @RequestParam int points) {
        return ResponseEntity.ok(commentService.addRecognitionPoints(feedbackId, points));
    }
    @DeleteMapping("/{feedbackId}") public ResponseEntity < Void > deleteFeedback(@PathVariable String feedbackId) {
        commentService.deleteComment(feedbackId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/idea/{ideaId}/count") public ResponseEntity < Integer > countFeedbackForIdea(@PathVariable String ideaId) {
        return ResponseEntity.ok(commentService.countCommentForIdea(ideaId));
    }
}