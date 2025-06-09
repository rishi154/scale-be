package com.globalpayments.scale.service;

import com.globalpayments.scale.dao.CampaignDao;
import com.globalpayments.scale.dao.IdeaDao;
import com.globalpayments.scale.dao.UserDao;
import com.globalpayments.scale.dto.CampaignDto;
import com.globalpayments.scale.dto.CreateCampaignRequest;
import com.globalpayments.scale.exception.ResourceNotFoundException;
import com.globalpayments.scale.model.Campaign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampaignService {

    private final CampaignDao campaignDao;
    private final UserDao userDao;
    private final IdeaDao ideaDao;

    @Autowired
    public CampaignService(CampaignDao campaignDao, UserDao userDao, IdeaDao ideaDao) {
        this.campaignDao = campaignDao;
        this.userDao = userDao;
        this.ideaDao = ideaDao;
    }

    public CampaignDto createCampaign(CreateCampaignRequest request, String ownerId) {
        if (!userDao.findById(ownerId).isPresent()) {
            throw new ResourceNotFoundException("User not found with ID: " + ownerId);
        }

        Campaign campaign = new Campaign();
        campaign.setTitle(request.getTitle());
        campaign.setDescription(request.getDescription());
        campaign.setTheme(request.getTheme());
        campaign.setStartDate(request.getStartDate());
        campaign.setEndDate(request.getEndDate());
        campaign.setOwnerId(ownerId);
        campaign.setStatus(request.getStatus());
        campaign.setTargetAudience(request.getTargetAudience());
        campaign.setAiGeneratedContent(request.getAiGeneratedContent());

        String campaignId = campaignDao.create(campaign);

        return campaignDao.findById(campaignId)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Failed to create campaign"));
    }

    public CampaignDto getCampaignById(String campaignId) {
        Campaign campaign = campaignDao.findById(campaignId)
                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found with ID: " + campaignId));

        return mapToDto(campaign);
    }

    public List<CampaignDto> getCampaignsByOwner(String ownerId) {
        if (!userDao.findById(ownerId).isPresent()) {
            throw new ResourceNotFoundException("User not found with ID: " + ownerId);
        }

        return campaignDao.findByOwnerId(ownerId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CampaignDto> getCampaignsByStatus(Campaign.CampaignStatus status) {
        return campaignDao.findByStatus(status).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CampaignDto> getAllCampaigns() {
        return campaignDao.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CampaignDto> getActiveOrUpcomingCampaigns() {
        return campaignDao.findActiveOrUpcoming().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public CampaignDto updateCampaign(String campaignId, CreateCampaignRequest request) {
        Campaign campaign = campaignDao.findById(campaignId)
                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found with ID: " + campaignId));

        if (request.getTitle() != null) {
            campaign.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            campaign.setDescription(request.getDescription());
        }

        if (request.getTheme() != null) {
            campaign.setTheme(request.getTheme());
        }

        if (request.getStartDate() != null) {
            campaign.setStartDate(request.getStartDate());
        }

        if (request.getEndDate() != null) {
            campaign.setEndDate(request.getEndDate());
        }

        if (request.getStatus() != null) {
            campaign.setStatus(request.getStatus());
        }

        if (request.getTargetAudience() != null) {
            campaign.setTargetAudience(request.getTargetAudience());
        }

        if (request.getAiGeneratedContent() != null) {
            campaign.setAiGeneratedContent(request.getAiGeneratedContent());
        }

        campaignDao.update(campaign);

        return mapToDto(campaign);
    }

    public CampaignDto updateCampaignStatus(String campaignId, Campaign.CampaignStatus status) {
        if (!campaignDao.updateStatus(campaignId, status)) {
            throw new ResourceNotFoundException("Campaign not found with ID: " + campaignId);
        }

        return campaignDao.findById(campaignId)
                .map(this::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found with ID: " + campaignId));
    }

    public void deleteCampaign(String campaignId) {
        if (!campaignDao.delete(campaignId)) {
            throw new ResourceNotFoundException("Campaign not found with ID: " + campaignId);
        }
    }

    private CampaignDto mapToDto(Campaign campaign) {
        CampaignDto dto = new CampaignDto();
        dto.setCampaignId(campaign.getCampaignId());
        dto.setTitle(campaign.getTitle());
        dto.setDescription(campaign.getDescription());
        dto.setTheme(campaign.getTheme());
        dto.setStartDate(campaign.getStartDate());
        dto.setEndDate(campaign.getEndDate());
        dto.setOwnerId(campaign.getOwnerId());
        dto.setStatus(campaign.getStatus());
        dto.setTargetAudience(campaign.getTargetAudience());
        dto.setAiGeneratedContent(campaign.getAiGeneratedContent());
        dto.setCreatedAt(campaign.getCreatedAt());
        dto.setUpdatedAt(campaign.getUpdatedAt());

        // Get owner's name if available
        userDao.findById(campaign.getOwnerId())
                .ifPresent(user -> dto.setOwnerName(user.getName()));

        // Count ideas submitted to this campaign
        int ideaCount = ideaDao.findByCampaignId(campaign.getCampaignId()).size();
        dto.setIdeaCount(ideaCount);

        return dto;
    }
}