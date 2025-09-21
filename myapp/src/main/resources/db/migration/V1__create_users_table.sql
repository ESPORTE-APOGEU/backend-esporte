-- USERS
CREATE TABLE IF NOT EXISTS users (
                                     id          VARCHAR(120) PRIMARY KEY,
    name        VARCHAR(120) NOT NULL,
    email       VARCHAR(120) NOT NULL UNIQUE,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    photo       VARCHAR(255),
    birthday    DATE,
    gender      VARCHAR(16),
    city        VARCHAR(120),
    CONSTRAINT chk_users_gender CHECK (gender IN ('Male','Female','Other'))
    );

-- ELEMENT COLLECTION (lista de strings)
CREATE TABLE IF NOT EXISTS user_sports (
                                           user_id VARCHAR(120) NOT NULL,
    sport   VARCHAR(60)  NOT NULL,
    PRIMARY KEY (user_id, sport),
    CONSTRAINT fk_user_sports_user
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );

CREATE INDEX IF NOT EXISTS idx_user_sports_user_id ON user_sports(user_id);
