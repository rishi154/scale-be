-- Users
CREATE TABLE IF NOT EXISTS users (
    user_id CHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('SUBMITTER', 'EVALUATOR', 'ADMIN', 'IDEA_OWNER') NOT NULL,
    department VARCHAR(100),
    language VARCHAR(10),
    profile_picture VARCHAR(255),
    ai_assistant_enabled BOOLEAN DEFAULT TRUE,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Campaigns / Challenges
CREATE TABLE IF NOT EXISTS campaigns (
    campaign_id CHAR(36) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    theme VARCHAR(100),
    start_date DATE,
    end_date DATE,
    owner_id CHAR(36),
    status ENUM('DRAFT', 'ACTIVE', 'COMPLETED', 'CANCELLED') DEFAULT 'DRAFT',
    target_audience TEXT,
    ai_generated_content JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES users(user_id)
);

-- Ideas
CREATE TABLE IF NOT EXISTS ideas (
    idea_id CHAR(36) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    submitted_by CHAR(36),
    submission_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    department VARCHAR(100),
    tags JSON,
    attachments JSON,
    votes_count INT DEFAULT 0,
    view_count INT DEFAULT 0,
    status ENUM('DRAFT', 'SUBMITTED', 'UNDER_REVIEW', 'APPROVED', 'REJECTED', 'IN_PROGRESS', 'LAUNCHED', 'EMBEDDED') DEFAULT 'SUBMITTED',
    stage ENUM('SOLICIT', 'CURATE', 'ASSESS', 'LAUNCH', 'EMBED') DEFAULT 'SOLICIT',
    language VARCHAR(10),
    ai_summary TEXT,
    ai_cluster_id VARCHAR(64),
    campaign_id CHAR(36),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (submitted_by) REFERENCES users(user_id),
    FOREIGN KEY (campaign_id) REFERENCES campaigns(campaign_id)
);

-- Evaluations
CREATE TABLE IF NOT EXISTS evaluations (
    evaluation_id CHAR(36) PRIMARY KEY,
    idea_id CHAR(36),
    reviewer_id CHAR(36),
    date DATETIME DEFAULT CURRENT_TIMESTAMP,
    roi_score INT,
    feasibility_score INT,
    alignment_score INT,
    risk_score INT,
    implementation_cost DECIMAL(12,2),
    total_score INT,
    review_comments TEXT,
    ai_score_suggestion INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (idea_id) REFERENCES ideas(idea_id),
    FOREIGN KEY (reviewer_id) REFERENCES users(user_id)
);

-- Projects (Post-Launch Ideas)
CREATE TABLE IF NOT EXISTS projects (
    project_id CHAR(36) PRIMARY KEY,
    idea_id CHAR(36),
    mvp_defined BOOLEAN DEFAULT FALSE,
    assigned_team JSON,
    budget_allocated DECIMAL(12,2),
    kpis JSON,
    status ENUM('PLANNING', 'IN_PROGRESS', 'COMPLETED', 'ON_HOLD') DEFAULT 'PLANNING',
    start_date DATE,
    end_date DATE,
    progress_updates JSON,
    ai_suggested_tech_stack TEXT,
    documentation_link TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (idea_id) REFERENCES ideas(idea_id)
);

-- Feedback & Recognition
CREATE TABLE IF NOT EXISTS feedback (
    feedback_id CHAR(36) PRIMARY KEY,
    idea_id CHAR(36),
    submitter_id CHAR(36),
    feedback_text TEXT,
    type ENUM('PRAISE', 'SUGGESTION', 'CONCERN') DEFAULT 'SUGGESTION',
    visibility ENUM('PUBLIC', 'PRIVATE') DEFAULT 'PUBLIC',
    recognition_points INT DEFAULT 0,
    badges_awarded JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (idea_id) REFERENCES ideas(idea_id),
    FOREIGN KEY (submitter_id) REFERENCES users(user_id)
);

-- AI Audit Logs
CREATE TABLE IF NOT EXISTS ai_audit_logs (
    action_id CHAR(36) PRIMARY KEY,
    user_id CHAR(36),
    action_type ENUM('SUMMARY', 'CLASSIFICATION', 'SCORE_SUGGESTION', 'LANGUAGE_TRANSLATION'),
    input_text TEXT,
    ai_output TEXT,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
