CREATE TABLE botlemon.items_reviews
(
    review_id SERIAL PRIMARY KEY,
    item_id    INT REFERENCES botlemon.items (item_id) NOT NULL,
    user_id    INT REFERENCES botlemon.users (user_id) NOT NULL,
    review     VARCHAR(225) DEFAULT NULL,
    rating     DECIMAL(3, 2) CHECK ( rating <= 5.0 AND rating >= 0.0 )
)