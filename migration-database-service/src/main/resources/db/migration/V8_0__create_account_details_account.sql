CREATE TABLE botlemon.account_details
(
    account_id            SERIAL PRIMARY KEY,
    last_login_date       TIMESTAMP,
--     total_purchased_items ,
    total_purchase_amount NUMERIC(5, 2) DEFAULT 0,
--     registration_date     TIMESTAMP,
--     avatar_url            VARCHAR(255),
    bonus_points          INT           DEFAULT 0,
    user_id               INT REFERENCES botlemon.users (user_id) ON DELETE CASCADE
)