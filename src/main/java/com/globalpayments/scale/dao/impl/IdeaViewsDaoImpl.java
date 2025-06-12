package com.globalpayments.scale.dao.impl;

import com.globalpayments.scale.dao.IdeaViewsDao;
import com.globalpayments.scale.model.IdeaView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class IdeaViewsDaoImpl implements IdeaViewsDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public IdeaViewsDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

//    private static final RowMapper<IdeaView> IDEA_VIEW_ROW_MAPPER = (rs, rowNum) -> {
//        IdeaView ideaView = new IdeaView();
//        ideaView.setUserId(rs.getString("user_id"));
//        ideaView.setIdeaId(rs.getString("idea_id"));
//        ideaView.setViewedAt(rs.getTimestamp("viewed_at").toLocalDateTime());
//        return ideaView;
//    };

    @Override
    public Optional<IdeaView> findByUserIdAndIdeaId(String userId, String ideaId) {
        try {
            String sql = "SELECT * FROM idea_views WHERE user_id = ? AND idea_id = ?";
            IdeaView ideaView = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                IdeaView view = new IdeaView();
                view.setUserId(rs.getString("user_id"));
                view.setIdeaId(rs.getString("idea_id"));
                view.setViewedAt(rs.getTimestamp("viewed_at").toLocalDateTime());
                return view;
            }, userId, ideaId);
            return Optional.ofNullable(ideaView);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<IdeaView> findByIdeaId(String ideaId) {
        try {
            String sql = "SELECT * FROM idea_views WHERE idea_id = ?";
            IdeaView ideaView = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                IdeaView view = new IdeaView();
                view.setUserId(rs.getString("user_id"));
                view.setIdeaId(rs.getString("idea_id"));
                view.setViewedAt(rs.getTimestamp("viewed_at").toLocalDateTime());
                return view;
            }, ideaId);
            return Optional.ofNullable(ideaView);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<IdeaView> findByUserId(String userId) {
        try {
            String sql = "SELECT * FROM idea_views WHERE user_id = ?";
            IdeaView ideaView = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                IdeaView view = new IdeaView();
                view.setUserId(rs.getString("user_id"));
                view.setIdeaId(rs.getString("idea_id"));
                view.setViewedAt(rs.getTimestamp("viewed_at").toLocalDateTime());
                return view;
            }, userId);
            return Optional.ofNullable(ideaView);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void create(IdeaView ideaView) {
        String sql = "INSERT INTO idea_views (user_id, idea_id, viewed_at) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, ideaView.getUserId(), ideaView.getIdeaId(), ideaView.getViewedAt());
    }
}