CREATE TABLE botlemon.items
(
    item_id          SERIAL PRIMARY KEY,
    name             VARCHAR(30)   NOT NULL,
    description      VARCHAR(30)                           DEFAULT 'Description empty',
    price            NUMERIC(10, 2) NOT NULL CHECK ( price > 0.0 ),
    publication_date TIMESTAMP
);