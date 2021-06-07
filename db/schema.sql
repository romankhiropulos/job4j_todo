DROP TABLE IF EXISTS item;

CREATE TABLE item
(
    id          SERIAL PRIMARY KEY,
    description VARCHAR   NOT NULL,
    created     TIMESTAMP NOT NULL,
    done        BOOLEAN   NOT NULL DEFAULT FALSE
);