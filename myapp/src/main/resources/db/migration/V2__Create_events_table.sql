CREATE TABLE events (
                        id          BIGSERIAL       PRIMARY KEY,
                        name        VARCHAR(255)    NOT NULL,
                        location    VARCHAR(255)    NOT NULL,
                        sport       VARCHAR(100)    NOT NULL,
                        level       VARCHAR(50)     NOT NULL,
                        gender      VARCHAR(50)     NOT NULL,
                        date        DATE            NOT NULL,
                        start_time  TIME            NOT NULL,
                        end_time    TIME            NOT NULL,
                        price       NUMERIC(10,2)   NOT NULL,
                        description TEXT
);
