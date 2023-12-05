CREATE TABLE botlemon.roles
(
    role_id   SERIAL PRIMARY KEY,
    role_name VARCHAR(10) NOT NULL UNIQUE
);

INSERT INTO botlemon.roles(role_name)
VALUES ('ADMIN'),
       ('USER');