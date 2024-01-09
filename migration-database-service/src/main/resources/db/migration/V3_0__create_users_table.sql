CREATE TABLE botlemon.users
(
    user_id         SERIAL PRIMARY KEY,
    username        VARCHAR(30) NOT NULL UNIQUE,
    password        VARCHAR(100) NOT NULL,
    first_name      VARCHAR(30) NOT NULL,
    last_name       VARCHAR(30) NOT NULL,
    enabled         BOOLEAN   DEFAULT FALSE,
    registration_at TIMESTAMP WITH TIME ZONE DEFAULT NOW() AT TIME ZONE 'Europe/Moscow',
    role_id         INT REFERENCES botlemon.roles (role_id) DEFAULT 2
);