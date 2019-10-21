DELETE FROM user_roles;
DELETE FROM users;

ALTER SEQUENCE global_seq RESTART WITH 100000;
ALTER SEQUENCE meals_id_seq RESTART WITH 1;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('ROLE_USER', 100000),
       ('ROLE_ADMIN', 100001);

INSERT INTO meals(datetime, description, calories, user_id)
VALUES ('2019-10-20 09:00:00', 'breakfast', 500, 100000),
       ('2019-10-20 13:00:00', 'lunch', 1000, 100000),
       ('2019-10-20 18:00:00', 'dinner', 500, 100000),
       ('2019-9-21 13:00:00', 'lunch', 1000, 100000);
