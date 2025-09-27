-- Flyway migration to create reports table
CREATE TABLE IF NOT EXISTS reports (
  id BIGSERIAL PRIMARY KEY,
  reporter_id varchar(255) NOT NULL,
  reported_user_id varchar(255),
  type varchar(100) NOT NULL,
  description text,
  created_at timestamp without time zone DEFAULT now(),
  CONSTRAINT fk_reports_reporter FOREIGN KEY (reporter_id) REFERENCES users(id),
  CONSTRAINT fk_reports_reported_user FOREIGN KEY (reported_user_id) REFERENCES users(id),
  CONSTRAINT fk_reports_event FOREIGN KEY (reported_user_id) REFERENCES users(id)
);
