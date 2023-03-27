

INSERT INTO country(name, shipping_multiplier)
VALUES ('Denmark', 0),
       ('Norway', 0),
       ('Sweden', 0),
       ('Germany', 2);

INSERT INTO box_tier(name, weight)
VALUES ('Basic', 1),
       ('Humble', 2),
       ('Deluxe', 5),
       ('Premium', 8);

INSERT INTO account(provider_id, email, created_at, dob, first_name, last_name, country_id, zip_code,
                    contact_number)
VALUES ('54457611-a61e-4f63-8a00-babda736e8d3', 'admin@example.com', '2023-01-01', '1990-01-01', 'Admin', 'Adminton', 1,
        '00100', '+3582211445'),
       ('1f41ae28-c277-4dcf-90b9-2e9009127e1f', 'user@example.com', '2023-01-01', '1990-01-01', 'User', 'Non-admin', 1,
        '00100', '+3582211445');

INSERT INTO shipment(account_id, box_tier_id, box_color, country_id, cost, recipient)
VALUES (1, 1, '#FFFFFF', 1, 100, 'User name');

INSERT INTO shipment_status(shipment_id, status, ts)
VALUES (1, 1, '2023-01-01');

INSERT INTO fee(name, amount)
VALUES ('Base shipping fee', 200);