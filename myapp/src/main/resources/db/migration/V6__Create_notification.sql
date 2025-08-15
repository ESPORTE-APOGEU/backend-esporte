-- V8__create_notification.sql
CREATE TABLE IF NOT EXISTS notification (
                                            id               BIGSERIAL PRIMARY KEY,
                                            user_id          BIGINT NOT NULL,
                                            related_event_id BIGINT,
                                            type             VARCHAR(50)  NOT NULL,
    icon_name        VARCHAR(50),
    tag_icon         VARCHAR(50),
    tag_text         VARCHAR(255),
    title            VARCHAR(255) NOT NULL,
    description      TEXT         NOT NULL,
    "timestamp"      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    read_flag        BOOLEAN      DEFAULT FALSE
    );

ALTER TABLE notification
    ADD CONSTRAINT fk_notification_user
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

-- Se sua tabela de eventos chama 'events' (como na sua entidade):
ALTER TABLE notification
    ADD CONSTRAINT fk_notification_event
        FOREIGN KEY (related_event_id) REFERENCES events(id) ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS idx_notification_user_ts ON notification (user_id, "timestamp" DESC);
