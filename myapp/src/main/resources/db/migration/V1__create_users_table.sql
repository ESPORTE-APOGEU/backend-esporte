CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(120) NOT NULL,
                       email VARCHAR(120) UNIQUE NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
