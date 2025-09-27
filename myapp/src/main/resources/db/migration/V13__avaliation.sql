ALTER TABLE users
    ADD COLUMN IF NOT EXISTS total_skill INTEGER;

ALTER TABLE users
    ADD COLUMN IF NOT EXISTS total_received_evaluations INTEGER DEFAULT 0;

ALTER TABLE users
    ADD COLUMN IF NOT EXISTS total_rating INTEGER DEFAULT 0;

CREATE TABLE IF NOT EXISTS avaliations (
                                           id              BIGSERIAL       PRIMARY KEY,
                                           event_id        BIGINT          NOT NULL,
                                           from_user_id    VARCHAR(120)    NOT NULL,
    to_user_id      VARCHAR(120)    NOT NULL,

    -- Mapeia o enum Status, com 'PENDING' como padrão
    status          VARCHAR(20)     NOT NULL DEFAULT 'PENDING',

    -- Mapeia o enum SkillLevel, pode ser nulo
    skill_level     VARCHAR(20),

    rating          INTEGER, -- para a nota (ex: 1 a 5)
    comment         TEXT,

    requested_at    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    responded_at    TIMESTAMP,

    -- Regras para garantir que os valores dos enums sejam válidos
    CONSTRAINT chk_avaliations_status
    CHECK (status IN ('PENDING', 'COMPLETED')),

    CONSTRAINT chk_avaliations_skill_level
    CHECK (skill_level IN ('INICIANTE', 'INTERMEDIARIO', 'AVANCADO', 'SEMIPROFISSIONAL')),

    -- Chaves estrangeiras para conectar com outras tabelas
    CONSTRAINT fk_avaliations_event
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,

    CONSTRAINT fk_avaliations_from_user
    FOREIGN KEY (from_user_id) REFERENCES users(id) ON DELETE CASCADE,

    CONSTRAINT fk_avaliations_to_user
    FOREIGN KEY (to_user_id) REFERENCES users(id) ON DELETE CASCADE
    );

-- Índices para acelerar buscas nas colunas de chaves estrangeiras
CREATE INDEX IF NOT EXISTS idx_avaliations_event_id     ON avaliations(event_id);
CREATE INDEX IF NOT EXISTS idx_avaliations_from_user_id ON avaliations(from_user_id);
CREATE INDEX IF NOT EXISTS idx_avaliations_to_user_id   ON avaliations(to_user_id);

ALTER TABLE events ADD COLUMN avaliations_requested BOOLEAN DEFAULT FALSE;

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='users' AND column_name='skill_level') THEN
UPDATE users SET total_skill = skill_level WHERE total_skill IS NULL;
END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='users' AND column_name='rating') THEN
UPDATE users SET total_rating = rating WHERE total_rating IS NULL;
END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='users' AND column_name='received_evaluations_count') THEN
UPDATE users SET total_received_evaluations = received_evaluations_count WHERE total_received_evaluations IS NULL;
END IF;
END
$$;

