ALTER TABLE users ADD COLUMN IF NOT EXISTS total_skill INTEGER;
ALTER TABLE users ADD COLUMN IF NOT EXISTS total_rating INTEGER DEFAULT 0;
ALTER TABLE users ADD COLUMN IF NOT EXISTS total_received_evaluations INTEGER DEFAULT 0;

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
