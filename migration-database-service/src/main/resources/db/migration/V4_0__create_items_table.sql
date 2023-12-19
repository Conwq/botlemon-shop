CREATE TABLE botlemon.items
(
    item_id          SERIAL PRIMARY KEY,
    name             VARCHAR(30)   NOT NULL,
    description      VARCHAR(30)                           DEFAULT 'Description empty',
    price            NUMERIC(5, 2) NOT NULL CHECK ( price > 0.0 ),
    rating           NUMERIC(1, 1) CHECK ( rating >= 0.0 ) DEFAULT 0.0,
    voters           INT                                   DEFAULT 0,
    publication_date TIMESTAMP
);