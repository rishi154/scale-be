package com.globalpayments.scale.dao;

import com.globalpayments.scale.model.Campaign;

import java.util.List;
import java.util.Optional;

public interface CampaignDao {
    String create(Campaign campaign);
    Optional<Campaign> findById(String campaignId);
    List<Campaign> findByOwnerId(String ownerId);
    List<Campaign> findByStatus(Campaign.CampaignStatus status);
    List<Campaign> findAll();
    List<Campaign> findActiveOrUpcoming();
    void update(Campaign campaign);
    boolean updateStatus(String campaignId, Campaign.CampaignStatus status);
    boolean delete(String campaignId);
}