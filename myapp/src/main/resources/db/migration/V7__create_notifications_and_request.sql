
CREATE TABLE IF NOT EXISTS event_entry (
                                           id            BIGSERIAL PRIMARY KEY,
                                           user_id       VARCHAR(191)  NOT NULL,
    event_id      BIGINT        NOT NULL,
    requested_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status        VARCHAR(20)   NOT NULL DEFAULT 'PENDING',
    CONSTRAINT ck_event_entry_status CHECK (status IN ('PENDING','ACCEPTED','DECLINED')),
    CONSTRAINT uq_event_entry_user_event UNIQUE (user_id, event_id),
    CONSTRAINT fk_event_entry_user  FOREIGN KEY (user_id)  REFERENCES users(id)  ON DELETE CASCADE,
    CONSTRAINT fk_event_entry_event FOREIGN KEY (event_id)  REFERENCES events(id) ON DELETE CASCADE
    -- Se sua tabela de eventos for "events", troque o FK acima para REFERENCES events(id)
    );

CREATE INDEX IF NOT EXISTS idx_event_entry_user_id  ON event_entry (user_id);
CREATE INDEX IF NOT EXISTS idx_event_entry_event_id ON event_entry (event_id);
CREATE INDEX IF NOT EXISTS idx_event_entry_status   ON event_entry (status);
CREATE INDEX IF NOT EXISTS idx_event_entry_req_at   ON event_entry (requested_at);

-- NOTIFICATION
CREATE TABLE IF NOT EXISTS notification (
                                            id               BIGSERIAL PRIMARY KEY,
                                            user_id          VARCHAR(191)  NOT NULL,
    type             VARCHAR(50)   NOT NULL,
    icon_name        VARCHAR(50),
    title            VARCHAR(120)  NOT NULL,
    description      VARCHAR(500)  NOT NULL,
    timestamp        TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tag_text         VARCHAR(40),
    tag_icon         VARCHAR(40),
    related_event_id BIGINT,
    entry_id         BIGINT,

    CONSTRAINT fk_notification_user           FOREIGN KEY (user_id)          REFERENCES users(id)        ON DELETE CASCADE,
    CONSTRAINT fk_notification_related_event  FOREIGN KEY (related_event_id) REFERENCES events(id)        ON DELETE SET NULL,
    CONSTRAINT fk_notification_entry          FOREIGN KEY (entry_id)         REFERENCES event_entry(id)  ON DELETE SET NULL
    -- Se sua tabela de eventos for "events", troque o FK acima para REFERENCES events(id)
    );

CREATE INDEX IF NOT EXISTS idx_notification_user_id          ON notification (user_id);
CREATE INDEX IF NOT EXISTS idx_notification_related_event_id ON notification (related_event_id);
CREATE INDEX IF NOT EXISTS idx_notification_entry_id         ON notification (entry_id);
CREATE INDEX IF NOT EXISTS idx_notification_timestamp        ON notification (timestamp);
