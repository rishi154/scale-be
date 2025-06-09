package com.globalpayments.scale.dao;


import com.globalpayments.scale.model.Idea;

import java.util.List;
import java.util.Optional;

public interface IdeaDao {
    String create(Idea idea);
    Optional<Idea> findById(String ideaId);
    List<Idea> findBySubmitterId(String submitterId);
    List<Idea> findByCampaignId(String campaignId);
    List<Idea> findByStatus(Idea.IdeaStatus status);
    List<Idea> findByStage(Idea.IdeaStage stage);
    List<Idea> findAll();
    List<Idea> findTopVotedIdeas(int limit);
    void update(Idea idea);
    boolean updateStatus(String ideaId, Idea.IdeaStatus status);
    boolean updateStage(String ideaId, Idea.IdeaStage stage);
    boolean delete(String ideaId);
    void incrementViewCount(String ideaId);
    void incrementVoteCount(String ideaId);
    void decrementVoteCount(String ideaId);
}