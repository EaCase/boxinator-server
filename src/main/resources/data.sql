INSERT INTO country_tier(name, shipping_multiplier)
VALUES ('Source', 0),
       ('Tier 1', 5),
       ('Tier 2', 10),
       ('Tier 3', 15);

INSERT INTO country(name, country_tier_id)
VALUES ('Denmark', 1),
       ('Norway', 1),
       ('Sweden', 1),
       ('Germany', 2);

INSERT INTO box_tier(name, weight)
VALUES ('Basic', 1),
       ('Humble', 2),
       ('Deluxe', 5),
       ('Premium', 8);

INSERT INTO account(provider_id, email, created_at, account_type, dob, first_name, last_name, country_id, zip_code,
                    contact_number)
VALUES ('12345qwerty', 'testmail@gmail.com', null, 2, null, 'Admin', 'Adminton', 1, null, '+3582211445');


INSERT INTO shipment(account_id, box_tier_id, box_color, country_id, cost)
VALUES (1, 1, '#FFFFFF', 1, 100);

INSERT INTO shipment_status(shipment_id, status, ts)
VALUES (1, 1, null);

INSERT INTO fee(name, amount)
VALUES ('Base shipping fee', 200);