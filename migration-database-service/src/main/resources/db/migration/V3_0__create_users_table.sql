CREATE TABLE botlemon.users
(
    user_id         SERIAL PRIMARY KEY,
    username        VARCHAR(30) NOT NULL UNIQUE,
    password        VARCHAR(100) NOT NULL,
    first_name      VARCHAR(30) NOT NULL,
    last_name       VARCHAR(30) NOT NULL,
    enabled         BOOLEAN   DEFAULT FALSE,
    registration_at TIMESTAMP DEFAULT NOW(),
    role_id         INT REFERENCES botlemon.roles (role_id) DEFAULT 2
);

INSERT INTO botlemon.users(username, password, first_name, last_name, role_id)
VALUES ('a', '$2a$10$lROYPWShj1vIGfnvXDlJOe8cBOnowm6d2Mb4p5xNDddbziNjhJbIa', 'a', 'a', 1);