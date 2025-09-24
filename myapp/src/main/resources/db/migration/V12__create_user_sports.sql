CREATE TABLE user_sports (
    user_id BIGINT NOT NULL,
    sports  VARCHAR(120) NOT NULL,
    CONSTRAINT fk_user_sports_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX ux_user_sports_user_sport ON user_sports(user_id, sports);
CREATE INDEX ix_user_sports_user ON user_sports(user_id);