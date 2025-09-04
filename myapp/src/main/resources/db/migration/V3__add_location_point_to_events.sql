ALTER TABLE events
ADD COLUMN location_point geography(Point, 4326);

ALTER TABLE users ADD COLUMN photo VARCHAR(255);