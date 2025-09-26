-- VXX__add_tag_url_to_notification.sql
ALTER TABLE notification
ADD COLUMN tag_url VARCHAR(500);
