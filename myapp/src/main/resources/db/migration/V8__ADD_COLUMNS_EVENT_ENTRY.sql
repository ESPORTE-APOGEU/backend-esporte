ALTER TABLE event_entry
    ADD COLUMN IF NOT EXISTS requester_name  VARCHAR(120);

ALTER TABLE event_entry
    ADD COLUMN IF NOT EXISTS requester_photo VARCHAR(500);
