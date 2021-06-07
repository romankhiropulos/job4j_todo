DELETE FROM item;

INSERT INTO item (description, created, done)
VALUES ('Пройти параграф Hibernate', now(), FALSE),
       ('Пройти параграф Spring', make_timestamp(2021, 7, 1, 8, 15, 23.5), FALSE),
       ('Пройти параграф SQL',make_timestamp(2021, 2, 5, 6, 25, 21.2), TRUE);