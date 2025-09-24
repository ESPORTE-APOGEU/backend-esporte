CREATE TABLE events (
                        id                  BIGSERIAL       PRIMARY KEY,
                        name                VARCHAR(255)    NOT NULL,
                        location            VARCHAR(255)    NOT NULL,
                        level               VARCHAR(50)     NOT NULL,
                        sport               VARCHAR(100)    NOT NULL, -- << COLUNA CORRETA ADICIONADA AQUI
                        gender              VARCHAR(50)     NOT NULL,
                        date                DATE            NOT NULL,
                        start_time          TIME            NOT NULL,
                        end_time            TIME            NOT NULL,
                        price               NUMERIC(10, 2)  NOT NULL,
                        description         TEXT,
                        whatsapp_link       VARCHAR(255),
                        cover_image_url       VARCHAR(500),
                        is_private          BOOLEAN         NOT NULL DEFAULT FALSE,
                        min_participants    INTEGER         NOT NULL,
                        max_participants    INTEGER         NOT NULL,
                        creator_id          VARCHAR(120)    NOT NULL,
                        location_point      GEOGRAPHY(Point, 4326),
                        CONSTRAINT fk_events_creator
                            FOREIGN KEY (creator_id) REFERENCES users(id) ON DELETE CASCADE
);

