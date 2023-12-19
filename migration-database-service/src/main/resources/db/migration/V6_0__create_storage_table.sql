CREATE TABLE botlemon.storage
(
    storage_id SERIAL PRIMARY KEY,
    item_id    INT REFERENCES botlemon.items (item_id) NOT NULL,
    quantity   INT CHECK (quantity >= 0) DEFAULT 0
);