package com.globalpayments.scale.service;

import com.globalpayments.scale.dao.CommentDao;
import com.globalpayments.scale.dao.IdeaDao;
import com.globalpayments.scale.dao.UserDao;
import com.globalpayments.scale.dto.CreateCommentRequest;
import com.globalpayments.scale.dto.CommentDto;
import com.globalpayments.scale.exception.ResourceNotFoundException;
import com.globalpayments.scale.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentDao commentDao;
    private final UserDao userDao;
    private final IdeaDao ideaDao;

    @Autowired
    public CommentService(CommentDao commentDao, UserDao userDao, IdeaDao ideaDao) {
        this.commentDao = commentDao;
        this.userDao = userDao;
        this.ideaDao = ideaDao;
    }

    public CommentDto createComment(CreateCommentRequest request, String submitterId) {
        if (!userDao.findById(submitterId).isPresent()) {
            throw new ResourceNotFoundException("User not found with ID: " + submitterId);
        }
        if (!ideaDao.findById(request.getIdeaId()).isPresent()) {
            throw new ResourceNotFoundException("Idea not found with ID: " + request.getIdeaId());
        }
        Comment comment = new Comment();
        comment.setIdeaId(request.getIdeaId());
        comment.setSubmitterId(submitterId);
        comment.setCommentText(request.getCommentText());
        comment.setType(request.getType());
        comment.setVisibility(request.getVisibility());
        comment.setRecognitionPoints(0);
        comment.setBadgesAwarded(request.getBadgesAwarded());
        String commentId = commentDao.create(comment);
        return commentDao.findById(commentId).map(this::mapToDto).orElseThrow(() -> new RuntimeException("Failed to create comment"));
    }

    public CommentDto getCommentById(String commentId) {
        Comment comment = commentDao.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment not found with ID: " + commentId));
        return mapToDto(comment);
    }

    public List<CommentDto> getCommentByIdea(String ideaId) {
        if (!ideaDao.findById(ideaId).isPresent()) {
            throw new ResourceNotFoundException("Idea not found with ID: " + ideaId);
        }
        return commentDao.findByIdeaId(ideaId).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<CommentDto> getCommentBySubmitter(String submitterId) {
        if (!userDao.findById(submitterId).isPresent()) {
            throw new ResourceNotFoundException("User not found with ID: " + submitterId);
        }
        return commentDao.findBySubmitterId(submitterId).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<CommentDto> getCommentByType(Comment.CommentType type) {
        return commentDao.findByType(type).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<CommentDto> getAllComment() {
        return commentDao.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public CommentDto updateComment(String commentId, CreateCommentRequest request) {
        Comment comment = commentDao.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment not found with ID: " + commentId));
        if (!comment.getIdeaId().equals(request.getIdeaId())) {
            throw new IllegalArgumentException("Cannot change the idea associated with comment");
        }
        if (request.getCommentText() != null) {
            comment.setCommentText(request.getCommentText());
        }
        if (request.getType() != null) {
            comment.setType(request.getType());
        }
        if (request.getVisibility() != null) {
            comment.setVisibility(request.getVisibility());
        }
        if (request.getBadgesAwarded() != null) {
            comment.setBadgesAwarded(request.getBadgesAwarded());
        }
        commentDao.update(comment);
        return mapToDto(comment);
    }

    public CommentDto addRecognitionPoints(String commentId, int points) {
        Comment comment = commentDao.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment not found with ID" + commentId));
        int newPoints = commentDao.addRecognitionPoints(commentId, points);
        comment.setRecognitionPoints(newPoints);
        return mapToDto(comment);
    }

    public void deleteComment(String commentId) {
        if (!commentDao.delete(commentId)) {
            throw new ResourceNotFoundException("Comment not found with ID: " + commentId);
        }
    }

    public int countCommentForIdea(String ideaId) {
        if (!ideaDao.findById(ideaId).isPresent()) {
            throw new ResourceNotFoundException("Idea not found with ID: " + ideaId);
        }
        return commentDao.countCommentsByIdeaId(ideaId);
    }

    private CommentDto mapToDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setCommentId(comment.getCommentId());
        dto.setIdeaId(comment.getIdeaId());
        dto.setSubmitterId(comment.getSubmitterId());
        dto.setCommentText(comment.getCommentText());
        dto.setType(comment.getType());
        dto.setVisibility(comment.getVisibility());
        dto.setRecognitionPoints(comment.getRecognitionPoints());
        dto.setBadgesAwarded(comment.getBadgesAwarded());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());
        userDao.findById(comment.getSubmitterId()).ifPresent(user -> dto.setSubmitterName(user.getName()));
        ideaDao.findById(comment.getIdeaId()).ifPresent(idea -> dto.setIdeaTitle(idea.getTitle()));
        return dto;
    }
}
