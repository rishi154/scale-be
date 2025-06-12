package com.globalpayments.scale.service;

import com.globalpayments.scale.dao.AiAuditLogDao;
import com.globalpayments.scale.dao.IdeaDao;
import com.globalpayments.scale.dao.IdeaViewsDao;
import com.globalpayments.scale.dao.UserDao;
import com.globalpayments.scale.dto.CreateIdeaRequest;
import com.globalpayments.scale.dto.IdeaDto;
import com.globalpayments.scale.exception.ResourceNotFoundException;
import com.globalpayments.scale.model.AiAuditLog;
import com.globalpayments.scale.model.Idea;
import com.globalpayments.scale.model.IdeaView;
import com.globalpayments.scale.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IdeaService {

    private final IdeaDao ideaDao;
    private final UserDao userDao;
    private final AiAuditLogDao aiAuditLogDao;
    private final IdeaViewsDao ideaViewsDao;

    @Autowired
    public IdeaService(IdeaDao ideaDao, UserDao userDao, AiAuditLogDao aiAuditLogDao, IdeaViewsDao ideaViewsDao) {
        this.ideaDao = ideaDao;
        this.userDao = userDao;
        this.aiAuditLogDao = aiAuditLogDao;
        this.ideaViewsDao = ideaViewsDao;
    }

    public IdeaDto createIdea(CreateIdeaRequest request, String submitterId) {
        User submitter = userDao.findById(submitterId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + submitterId));

        Idea idea = new Idea();
        idea.setTitle(request.getTitle());
        idea.setDescription(request.getDescription());
        idea.setSubmittedBy(submitterId);
        idea.setSubmissionDate(LocalDateTime.now());
        idea.setDepartment(request.getDepartment() != null ? request.getDepartment() : submitter.getDepartment());
        idea.setTags(request.getTags());
        idea.setAttachments(request.getAttachments());
        idea.setVotesCount(0);
        idea.setViewCount(0);
        idea.setStatus(request.getStatus() != null ? request.getStatus() : Idea.IdeaStatus.DRAFT);
        idea.setStage(Idea.IdeaStage.SOLICIT);
        idea.setLanguage(request.getLanguage() != null ? request.getLanguage() : submitter.getLanguage());
        idea.setCampaignId(request.getCampaignId());

        // Generate AI summary if needed
        if (submitter.getAiAssistantEnabled()) {
            // This would be a call to an AI service in a real implementation
            idea.setAiSummary(generateAiSummary(idea.getDescription()));

            // Log AI usage
            AiAuditLog log = new AiAuditLog();
            log.setUserId(submitterId);
            log.setActionType(AiAuditLog.ActionType.SUMMARY);
            log.setInputText(idea.getDescription());
            log.setAiOutput(idea.getAiSummary());
            aiAuditLogDao.create(log);
        }

        String ideaId = ideaDao.create(idea);

        return ideaDao.findById(ideaId)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Failed to create idea"));
    }

    public IdeaDto getIdeaById(String ideaId) {
        Idea idea = ideaDao.findById(ideaId)
                .orElseThrow(() -> new ResourceNotFoundException("Idea not found with ID: " + ideaId));

        Optional<IdeaView> byIdeaId = ideaViewsDao.findByIdeaId(ideaId);

        System.out.println("Found idea view: " + byIdeaId.isPresent());
//        System.out.println("Found idea view to string: " + byIdeaId.get().toString());
        System.out.println(userDao.toString());
        System.out.println(idea.toString());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        System.out.println(authentication.getPrincipal());

//        ideaViewsDao.findByUserIdAndIdeaId(ideaId, )
//        if (!byIdeaId.isPresent()) {
//            ideaDao.incrementViewCount(ideaId);
//            IdeaView ideaView = new IdeaView();
//            ideaView.setIdeaId(ideaId);
//
////            ideaView.setUserId();
//
//        }

        // Increment view count when getting an idea
        ideaDao.incrementViewCount(ideaId);

        return mapToDto(idea);
    }

    public List<IdeaDto> getIdeasBySubmitter(String submitterId) {
        if (!userDao.findById(submitterId).isPresent()) {
            throw new ResourceNotFoundException("User not found with ID: " + submitterId);
        }

        return ideaDao.findBySubmitterId(submitterId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<IdeaDto> getIdeasByCampaign(String campaignId) {
        return ideaDao.findByCampaignId(campaignId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<IdeaDto> getIdeasByStatus(Idea.IdeaStatus status) {
        return ideaDao.findByStatus(status).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<IdeaDto> getIdeasByStage(Idea.IdeaStage stage) {
        return ideaDao.findByStage(stage).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<IdeaDto> getAllIdeas() {
        return ideaDao.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<IdeaDto> getTopVotedIdeas(int limit) {
        return ideaDao.findTopVotedIdeas(limit).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public IdeaDto updateIdea(String ideaId, CreateIdeaRequest request) {
        Idea idea = ideaDao.findById(ideaId)
                .orElseThrow(() -> new ResourceNotFoundException("Idea not found with ID: " + ideaId));

        if (request.getTitle() != null) {
            idea.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            idea.setDescription(request.getDescription());
        }

        if (request.getDepartment() != null) {
            idea.setDepartment(request.getDepartment());
        }

        if (request.getTags() != null) {
            idea.setTags(request.getTags());
        }

        if (request.getAttachments() != null) {
            idea.setAttachments(request.getAttachments());
        }

        if (request.getStatus() != null) {
            idea.setStatus(request.getStatus());
        }

        if (request.getLanguage() != null) {
            idea.setLanguage(request.getLanguage());
        }

        if (request.getCampaignId() != null) {
            idea.setCampaignId(request.getCampaignId());
        }

        ideaDao.update(idea);

        return mapToDto(idea);
    }

    public IdeaDto updateIdeaStatus(String ideaId, Idea.IdeaStatus status) {
        if (!ideaDao.updateStatus(ideaId, status)) {
            throw new ResourceNotFoundException("Idea not found with ID: " + ideaId);
        }

        return ideaDao.findById(ideaId)
                .map(this::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Idea not found with ID: " + ideaId));
    }

    public IdeaDto updateIdeaStage(String ideaId, Idea.IdeaStage stage) {
        if (!ideaDao.updateStage(ideaId, stage)) {
            throw new ResourceNotFoundException("Idea not found with ID: " + ideaId);
        }

        return ideaDao.findById(ideaId)
                .map(this::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Idea not found with ID: " + ideaId));
    }

    public void deleteIdea(String ideaId) {
        if (!ideaDao.delete(ideaId)) {
            throw new ResourceNotFoundException("Idea not found with ID: " + ideaId);
        }
    }

    public void voteForIdea(String ideaId) {
        Idea idea = ideaDao.findById(ideaId)
                .orElseThrow(() -> new ResourceNotFoundException("Idea not found with ID: " + ideaId));

        ideaDao.incrementVoteCount(ideaId);
    }

    public void removeVoteFromIdea(String ideaId) {
        Idea idea = ideaDao.findById(ideaId)
                .orElseThrow(() -> new ResourceNotFoundException("Idea not found with ID: " + ideaId));

        ideaDao.decrementVoteCount(ideaId);
    }

    private String generateAiSummary(String description) {
        // In a real implementation, this would call an AI service
        // Here we're just creating a simple mock summary
        if (description.length() <= 100) {
            return description;
        }
        return description.substring(0, 100) + "...";
    }

    private IdeaDto mapToDto(Idea idea) {
        IdeaDto dto = new IdeaDto();
        dto.setIdeaId(idea.getIdeaId());
        dto.setTitle(idea.getTitle());
        dto.setDescription(idea.getDescription());
        dto.setSubmittedBy(idea.getSubmittedBy());
        dto.setSubmissionDate(idea.getSubmissionDate());
        dto.setDepartment(idea.getDepartment());
        dto.setTags(idea.getTags());
        dto.setAttachments(idea.getAttachments());
        dto.setVotesCount(idea.getVotesCount());
        dto.setViewCount(idea.getViewCount());
        dto.setStatus(idea.getStatus());
        dto.setStage(idea.getStage());
        dto.setLanguage(idea.getLanguage());
        dto.setAiSummary(idea.getAiSummary());
        dto.setAiClusterId(idea.getAiClusterId());
        dto.setCampaignId(idea.getCampaignId());
        dto.setCreatedAt(idea.getCreatedAt());
        dto.setUpdatedAt(idea.getUpdatedAt());

        // Get submitter's name if available
        userDao.findById(idea.getSubmittedBy())
                .ifPresent(user -> dto.setSubmitterName(user.getName()));

        return dto;
    }
}