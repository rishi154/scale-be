package com.globalpayments.scale.dao.impl;

import com.globalpayments.scale.dao.CampaignDao;
import com.globalpayments.scale.model.Campaign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CampaignDaoImpl implements CampaignDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CampaignDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<Campaign> CAMPAIGN_ROW_MAPPER = (rs, rowNum) -> {
        Campaign campaign = new Campaign();
        campaign.setCampaignId(rs.getString("campaign_id"));
        campaign.setTitle(rs.getString("title"));
        campaign.setDescription(rs.getString("description"));
        campaign.setTheme(rs.getString("theme"));

        Date startDate = rs.getDate("start_date");
        campaign.setStartDate(startDate != null ? startDate.toLocalDate() : null);

        Date endDate = rs.getDate("end_date");
        campaign.setEndDate(endDate != null ? endDate.toLocalDate() : null);

        campaign.setOwnerId(rs.getString("owner_id"));
        campaign.setStatus(Campaign.CampaignStatus.valueOf(rs.getString("status")));
        campaign.setTargetAudience(rs.getString("target_audience"));
        campaign.setAiGeneratedContent(rs.getString("ai_generated_content"));
        campaign.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        campaign.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return campaign;
    };

    @Override
    public String create(Campaign campaign) {
        String campaignId = UUID.randomUUID().toString();

        String sql = "INSERT INTO campaigns (campaign_id, title, description, theme, start_date, " +
                "end_date, owner_id, status, target_audience, ai_generated_content) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                campaignId,
                campaign.getTitle(),
                campaign.getDescription(),
                campaign.getTheme(),
                campaign.getStartDate() != null ? java.sql.Date.valueOf(campaign.getStartDate()) : null,
                campaign.getEndDate() != null ? java.sql.Date.valueOf(campaign.getEndDate()) : null,
                campaign.getOwnerId(),
                campaign.getStatus().toString(),
                campaign.getTargetAudience(),
                campaign.getAiGeneratedContent());

        return campaignId;
    }

    @Override
    public Optional<Campaign> findById(String campaignId) {
        try {
            String sql = "SELECT * FROM campaigns WHERE campaign_id = ?";
            Campaign campaign = jdbcTemplate.queryForObject(sql, CAMPAIGN_ROW_MAPPER, campaignId);
            return Optional.ofNullable(campaign);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Campaign> findByOwnerId(String ownerId) {
        String sql = "SELECT * FROM campaigns WHERE owner_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, CAMPAIGN_ROW_MAPPER, ownerId);
    }

    @Override
    public List<Campaign> findByStatus(Campaign.CampaignStatus status) {
        String sql = "SELECT * FROM campaigns WHERE status = ? ORDER BY start_date ASC, created_at DESC";
        return jdbcTemplate.query(sql, CAMPAIGN_ROW_MAPPER, status.toString());
    }

    @Override
    public List<Campaign> findAll() {
        String sql = "SELECT * FROM campaigns ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, CAMPAIGN_ROW_MAPPER);
    }

    @Override
    public List<Campaign> findActiveOrUpcoming() {
        String sql = "SELECT * FROM campaigns WHERE status = 'ACTIVE' OR " +
                "(status = 'DRAFT' AND start_date >= CURRENT_DATE()) " +
                "ORDER BY start_date ASC, created_at DESC";
        return jdbcTemplate.query(sql, CAMPAIGN_ROW_MAPPER);
    }

    @Override
    public void update(Campaign campaign) {
        String sql = "UPDATE campaigns SET title = ?, description = ?, theme = ?, " +
                "start_date = ?, end_date = ?, owner_id = ?, status = ?, " +
                "target_audience = ?, ai_generated_content = ? " +
                "WHERE campaign_id = ?";

        jdbcTemplate.update(sql,
                campaign.getTitle(),
                campaign.getDescription(),
                campaign.getTheme(),
                campaign.getStartDate() != null ? java.sql.Date.valueOf(campaign.getStartDate()) : null,
                campaign.getEndDate() != null ? java.sql.Date.valueOf(campaign.getEndDate()) : null,
                campaign.getOwnerId(),
                campaign.getStatus().toString(),
                campaign.getTargetAudience(),
                campaign.getAiGeneratedContent(),
                campaign.getCampaignId());
    }

    @Override
    public boolean updateStatus(String campaignId, Campaign.CampaignStatus status) {
        String sql = "UPDATE campaigns SET status = ? WHERE campaign_id = ?";
        return jdbcTemplate.update(sql, status.toString(), campaignId) > 0;
    }

    @Override
    public boolean delete(String campaignId) {
        String sql = "DELETE FROM campaigns WHERE campaign_id = ?";
        return jdbcTemplate.update(sql, campaignId) > 0;
    }
}