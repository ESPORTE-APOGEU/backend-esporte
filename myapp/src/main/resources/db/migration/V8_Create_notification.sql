CREATE TABLE notification (
    id               BIGSERIAL PRIMARY KEY,
    user_id          BIGINT NOT NULL,
    event_id         BIGINT,
    type             VARCHAR(50) NOT NULL,
    icon_name        VARCHAR(50),
    tag_icon         VARCHAR(50),
    tag_text         VARCHAR(255),
    title            VARCHAR(255) NOT NULL,
    description      TEXT NOT NULL,
    timestamp        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    related_event_id BIGINT,
    read_flag        BOOLEAN DEFAULT FALSE
);