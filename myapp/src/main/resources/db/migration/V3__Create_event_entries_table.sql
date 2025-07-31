CREATE TABLE event_entry (
    id          BIGSERIAL PRIMARY KEY,
    event_id    BIGINT NOT NULL,
    user_id     BIGINT NOT NULL,
    requested_at TIMESTAMP NOT NULL
);