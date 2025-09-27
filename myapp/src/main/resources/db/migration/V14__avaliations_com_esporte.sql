ALTER TABLE avaliations
  ADD COLUMN IF NOT EXISTS sport VARCHAR(80);

-- Se já tem avaliações antigas, preenche a partir do evento:
UPDATE avaliations a
SET sport = e.sport
FROM events e
WHERE a.event_id = e.id AND a.sport IS NULL;

-- Índice de suporte a consultas por esporte do avaliado:
CREATE INDEX IF NOT EXISTS idx_avaliations_to_sport
  ON avaliations (to_user_id, sport)
  WHERE status = 'COMPLETED';


CREATE TABLE IF NOT EXISTS user_sport_stats (
                                                id BIGSERIAL PRIMARY KEY,
                                                user_id VARCHAR(120) NOT NULL,
    sport VARCHAR(80) NOT NULL,
    total_skill INTEGER NOT NULL DEFAULT 0,
    total_received_evaluations INTEGER NOT NULL DEFAULT 0,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_sport_stats_user
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uq_user_sport UNIQUE (user_id, sport)
    );

CREATE INDEX IF NOT EXISTS idx_user_sport_stats_user
    ON user_sport_stats (user_id);
