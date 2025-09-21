CREATE TABLE address (
    address_id UUID PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    postal_code VARCHAR(255),
    city VARCHAR(255),
    state VARCHAR(255),
    district VARCHAR(255),
    street VARCHAR(255),
    number VARCHAR(255),
    complement VARCHAR(255),
    default_address BOOLEAN,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT fk_address_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);