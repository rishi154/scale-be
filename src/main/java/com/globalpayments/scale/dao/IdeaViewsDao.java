package com.globalpayments.scale.dao;

import com.globalpayments.scale.model.IdeaView;

import java.util.Optional;

public interface IdeaViewsDao {
    Optional<IdeaView> findByUserIdAndIdeaId(String userId, String ideaId);
    Optional<IdeaView> findByIdeaId(String ideaId);
    Optional<IdeaView> findByUserId(String userId);
    void create(IdeaView ideaView);
}


