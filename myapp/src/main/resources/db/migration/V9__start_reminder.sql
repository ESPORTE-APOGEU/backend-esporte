-- adiciona a coluna (nula por padrão)
ALTER TABLE events
ADD COLUMN IF NOT EXISTS start_reminder_sent_at timestamp without time zone;

-- índice opcional para acelerar o job que busca eventos sem lembrete enviado
-- a cláusula WHERE mantém o índice pequeno e focado no caso de uso
CREATE INDEX IF NOT EXISTS idx_events_start_reminder
  ON events (date, start_time, start_reminder_sent_at)
  WHERE start_reminder_sent_at IS NULL;
