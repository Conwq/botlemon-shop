ALTER TABLE botlemon.users ADD COLUMN email VARCHAR(100) UNIQUE;

UPDATE botlemon.users SET email = 'admin@mail.ru' WHERE user_id = 1;

ALTER TABLE botlemon.users ALTER COLUMN email SET NOT NULL;