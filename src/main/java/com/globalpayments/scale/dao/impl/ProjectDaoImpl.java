package com.globalpayments.scale.dao.impl;

import com.globalpayments.scale.dao.ProjectDao;
import com.globalpayments.scale.model.Project;
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
public class ProjectDaoImpl implements ProjectDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProjectDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<Project> PROJECT_ROW_MAPPER = (rs, rowNum) -> {
        Project project = new Project();
        project.setProjectId(rs.getString("project_id"));
        project.setIdeaId(rs.getString("idea_id"));
        project.setMvpDefined(rs.getBoolean("mvp_defined"));
        project.setAssignedTeam(rs.getString("assigned_team"));
        project.setBudgetAllocated(rs.getBigDecimal("budget_allocated"));
        project.setKpis(rs.getString("kpis"));
        project.setStatus(Project.ProjectStatus.valueOf(rs.getString("status")));

        Date startDate = rs.getDate("start_date");
        project.setStartDate(startDate != null ? startDate.toLocalDate() : null);

        Date endDate = rs.getDate("end_date");
        project.setEndDate(endDate != null ? endDate.toLocalDate() : null);

        project.setProgressUpdates(rs.getString("progress_updates"));
        project.setAiSuggestedTechStack(rs.getString("ai_suggested_tech_stack"));
        project.setDocumentationLink(rs.getString("documentation_link"));
        project.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        project.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return project;
    };

    @Override
    public String create(Project project) {
        String projectId = UUID.randomUUID().toString();

        String sql = "INSERT INTO projects (project_id, idea_id, mvp_defined, assigned_team, " +
                "budget_allocated, kpis, status, start_date, end_date, progress_updates, " +
                "ai_suggested_tech_stack, documentation_link) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                projectId,
                project.getIdeaId(),
                project.getMvpDefined(),
                project.getAssignedTeam(),
                project.getBudgetAllocated(),
                project.getKpis(),
                project.getStatus().toString(),
                project.getStartDate() != null ? java.sql.Date.valueOf(project.getStartDate()) : null,
                project.getEndDate() != null ? java.sql.Date.valueOf(project.getEndDate()) : null,
                project.getProgressUpdates(),
                project.getAiSuggestedTechStack(),
                project.getDocumentationLink());

        return projectId;
    }

    @Override
    public Optional<Project> findById(String projectId) {
        try {
            String sql = "SELECT * FROM projects WHERE project_id = ?";
            Project project = jdbcTemplate.queryForObject(sql, PROJECT_ROW_MAPPER, projectId);
            return Optional.ofNullable(project);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Project> findByIdeaId(String ideaId) {
        try {
            String sql = "SELECT * FROM projects WHERE idea_id = ?";
            Project project = jdbcTemplate.queryForObject(sql, PROJECT_ROW_MAPPER, ideaId);
            return Optional.ofNullable(project);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Project> findByStatus(Project.ProjectStatus status) {
        String sql = "SELECT * FROM projects WHERE status = ? ORDER BY start_date ASC, created_at DESC";
        return jdbcTemplate.query(sql, PROJECT_ROW_MAPPER, status.toString());
    }

    @Override
    public List<Project> findAll() {
        String sql = "SELECT * FROM projects ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, PROJECT_ROW_MAPPER);
    }

    @Override
    public void update(Project project) {
        String sql = "UPDATE projects SET mvp_defined = ?, assigned_team = ?, " +
                "budget_allocated = ?, kpis = ?, status = ?, start_date = ?, " +
                "end_date = ?, progress_updates = ?, ai_suggested_tech_stack = ?, " +
                "documentation_link = ? WHERE project_id = ?";

        jdbcTemplate.update(sql,
                project.getMvpDefined(),
                project.getAssignedTeam(),
                project.getBudgetAllocated(),
                project.getKpis(),
                project.getStatus().toString(),
                project.getStartDate() != null ? java.sql.Date.valueOf(project.getStartDate()) : null,
                project.getEndDate() != null ? java.sql.Date.valueOf(project.getEndDate()) : null,
                project.getProgressUpdates(),
                project.getAiSuggestedTechStack(),
                project.getDocumentationLink(),
                project.getProjectId());
    }

    @Override
    public boolean updateStatus(String projectId, Project.ProjectStatus status) {
        String sql = "UPDATE projects SET status = ? WHERE project_id = ?";
        return jdbcTemplate.update(sql, status.toString(), projectId) > 0;
    }

    @Override
    public boolean delete(String projectId) {
        String sql = "DELETE FROM projects WHERE project_id = ?";
        return jdbcTemplate.update(sql, projectId) > 0;
    }
}