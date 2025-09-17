ALTER TABLE notification
  ADD COLUMN entry_id BIGINT;

ALTER TABLE notification
  ADD CONSTRAINT fk_notification_entry
    FOREIGN KEY (entry_id) REFERENCES event_entry(id) ON DELETE SET NULL;