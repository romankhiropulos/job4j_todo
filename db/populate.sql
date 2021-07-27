DELETE FROM item;

INSERT INTO item (description, created, done, user_id)
VALUES ('Пройти параграф Hibernate', now(), FALSE, 1),
       ('Пройти параграф Spring', make_timestamp(2021, 7, 1, 8, 15, 23.5), FALSE, 1),
       ('Пройти параграф SQL',make_timestamp(2021, 2, 5, 6, 25, 21.2), TRUE, 1);

INSERT INTO category (id, name)
VALUES (1, 'Санкт-Петербург'),
       (2, 'Москва'),
       (3, 'Стокгольм'),
       (4, 'Нью-Йорк'),
       (5, 'Афины');