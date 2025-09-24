ALTER TABLE IF EXISTS events
  ADD COLUMN IF NOT EXISTS capacity INTEGER;

-- Opcional: valor default inicial para registros antigos
UPDATE events SET capacity = 10 WHERE capacity IS NULL;