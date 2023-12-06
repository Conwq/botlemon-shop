CREATE TABLE botlemon.carts
(
    cart_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES botlemon.users(user_id),
    item_id INT REFERENCES botlemon.items(item_id)
);