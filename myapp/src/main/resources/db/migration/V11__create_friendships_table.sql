-- V11__create_friendships_table.sql (Corrigido para PostgreSQL)

CREATE TABLE IF NOT EXISTS friendships (
                                           user1_id    VARCHAR(120) NOT NULL,
    user2_id    VARCHAR(120) NOT NULL,
    status      SMALLINT NOT NULL, -- ✅ CORREÇÃO: TINYINT foi trocado por SMALLINT
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Define a chave primária como a combinação das três colunas
    PRIMARY KEY (user1_id, user2_id, status),

    CONSTRAINT fk_friendships_user1
    FOREIGN KEY (user1_id) REFERENCES users(id) ON DELETE CASCADE,

    CONSTRAINT fk_friendships_user2
    FOREIGN KEY (user2_id) REFERENCES users(id) ON DELETE CASCADE,

    -- Garante que o ID menor sempre venha primeiro
    CONSTRAINT chk_friendship_user_order CHECK (user1_id < user2_id)
    );

CREATE INDEX IF NOT EXISTS idx_friendships_status ON friendships(status);
CREATE INDEX IF NOT EXISTS idx_friendships_user1_id ON friendships(user1_id);
CREATE INDEX IF NOT EXISTS idx_friendships_user2_id ON friendships(user2_id);