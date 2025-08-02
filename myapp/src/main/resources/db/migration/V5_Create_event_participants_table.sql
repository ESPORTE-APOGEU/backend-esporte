CREATE TABLE event_participants (
    id                BIGSERIAL PRIMARY KEY,
    event_id          BIGINT NOT NULL,
    user_id           BIGINT NOT NULL,
    participant_name  VARCHAR(255),
    participant_photo VARCHAR(255)
);