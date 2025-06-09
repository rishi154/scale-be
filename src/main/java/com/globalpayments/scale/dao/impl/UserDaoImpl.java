package com.globalpayments.scale.dao.impl;

import com.globalpayments.scale.dao.UserDao;
import com.globalpayments.scale.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private static final RowMapper<User> USER_ROW_MAPPER = (ResultSet rs, int rowNum) -> {
        User user = new User();
        user.setUserId(rs.getString("user_id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRole(User.UserRole.valueOf(rs.getString("role")));
        user.setDepartment(rs.getString("department"));
        user.setLanguage(rs.getString("language"));
        user.setProfilePicture(rs.getString("profile_picture"));
        user.setAiAssistantEnabled(rs.getBoolean("ai_assistant_enabled"));
        user.setActive(rs.getBoolean("active"));
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return user;
    };

    @Override
    public String create(User user) {
        String userId = UUID.randomUUID().toString();
        String sql = "INSERT INTO users (user_id, name, email, password, role, department, language, profile_picture, " +
                "ai_assistant_enabled, active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                userId,
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getRole().toString(),
                user.getDepartment(),
                user.getLanguage(),
                user.getProfilePicture(),
                user.getAiAssistantEnabled(),
                user.getActive());

        return userId;
    }

    @Override
    public Optional<User> findById(String userId) {
        try {
            String sql = "SELECT * FROM users WHERE user_id = ?";
            User user = jdbcTemplate.queryForObject(sql, USER_ROW_MAPPER, userId);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            String sql = "SELECT * FROM users WHERE email = ?";
            User user = jdbcTemplate.queryForObject(sql, USER_ROW_MAPPER, email);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, USER_ROW_MAPPER);
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET name = ?, email = ?, role = ?, department = ?, " +
                "language = ?, profile_picture = ?, ai_assistant_enabled = ?, active = ? " +
                "WHERE user_id = ?";

        jdbcTemplate.update(sql,
                user.getName(),
                user.getEmail(),
                user.getRole().toString(),
                user.getDepartment(),
                user.getLanguage(),
                user.getProfilePicture(),
                user.getAiAssistantEnabled(),
                user.getActive(),
                user.getUserId());
    }

    @Override
    public void updatePassword(String userId, String password) {
        String sql = "UPDATE users SET password = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, password, userId);
    }

    @Override
    public boolean delete(String userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        return jdbcTemplate.update(sql, userId) > 0;
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }
}