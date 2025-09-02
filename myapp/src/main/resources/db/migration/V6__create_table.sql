CREATE TABLE friend_requests (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    CONSTRAINT fk_sender FOREIGN KEY (sender_id)
        REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_receiver FOREIGN KEY (receiver_id)
        REFERENCES users (id) ON DELETE CASCADE
);