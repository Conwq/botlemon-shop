CREATE TABLE botlemon.orders
(
    order_id   SERIAL PRIMARY KEY,
    user_id    INT REFERENCES botlemon.users (user_id) NOT NULL,
    order_date TIMESTAMP DEFAULT current_timestamp
);


CREATE TABLE botlemon.orders_items
(
    orders_items_id SERIAL PRIMARY KEY,
    order_id        INT NOT NULL,
    item_id         INT NOT NULL,
    quantity        INT NOT NULL CHECK ( quantity > 0 ),
    FOREIGN KEY (order_id) REFERENCES botlemon.orders (order_id),
    FOREIGN KEY (item_id) REFERENCES botlemon.items (item_id)
);