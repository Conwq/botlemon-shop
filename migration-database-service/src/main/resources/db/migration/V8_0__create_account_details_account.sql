CREATE TABLE botlemon.account_details
(
    account_id            SERIAL PRIMARY KEY,
    last_login_date       TIMESTAMP,
--     total_purchased_items ,
    total_purchase_amount NUMERIC(10, 2) DEFAULT 0,
--     registration_date     TIMESTAMP,
--     avatar_url            VARCHAR(255),
    discount_percentage          INT           DEFAULT 0,
    user_id               INT REFERENCES botlemon.users (user_id) ON DELETE CASCADE
);



CREATE OR REPLACE FUNCTION update_discount_percentage() RETURNS TRIGGER AS
$$
BEGIN
    NEW.discount_percentage :=
            CASE
                WHEN NEW.total_purchase_amount BETWEEN 1000 AND 1999 THEN 1
                WHEN NEW.total_purchase_amount BETWEEN 2000 AND 2999 THEN 2
                WHEN NEW.total_purchase_amount BETWEEN 3000 AND 3999 THEN 3
                WHEN NEW.total_purchase_amount BETWEEN 4000 AND 4999 THEN 4
                WHEN NEW.total_purchase_amount BETWEEN 5000 AND 5999 THEN 5
                WHEN NEW.total_purchase_amount BETWEEN 6000 AND 6999 THEN 6
                WHEN NEW.total_purchase_amount BETWEEN 7000 AND 7999 THEN 7
                WHEN NEW.total_purchase_amount BETWEEN 8000 AND 8999 THEN 8
                WHEN NEW.total_purchase_amount BETWEEN 9000 AND 9999 THEN 9
                WHEN NEW.total_purchase_amount >= 10000 THEN 10
                ELSE 0
                END;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;



CREATE TRIGGER update_discount_percentage_trigger
    BEFORE INSERT OR UPDATE OF total_purchase_amount
    ON botlemon.account_details
    FOR EACH ROW
EXECUTE FUNCTION update_discount_percentage();