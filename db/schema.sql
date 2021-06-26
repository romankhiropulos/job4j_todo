DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS j_user;

CREATE TABLE item
(
    id          SERIAL PRIMARY KEY,
    description VARCHAR   NOT NULL,
    created     TIMESTAMP NOT NULL,
    done        BOOLEAN   NOT NULL DEFAULT FALSE,
    user_id     SERIAL    NOT NULL REFERENCES j_user (id)
);

CREATE TABLE j_user
(
    id   SERIAL PRIMARY KEY,
    login VARCHAR(2000),
    password varchar(2000)
);