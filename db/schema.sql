DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS j_user;

CREATE TABLE item
(
    id          SERIAL PRIMARY KEY,
    description VARCHAR   NOT NULL,
    created     TIMESTAMP NOT NULL,
    done        BOOLEAN   NOT NULL DEFAULT FALSE,
    role_id int not null references j_user(id)
);

create table j_user (
                        id serial primary key,
                        name varchar(2000)
);