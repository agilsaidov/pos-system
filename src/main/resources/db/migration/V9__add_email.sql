ALTER TABLE users
ADD COLUMN email VARCHAR(255) DEFAULT 'default@mail.com';

UPDATE users SET email='admin@pos.com' WHERE id = 9;
UPDATE users SET email='manager1@pos.com' WHERE id = 10;
UPDATE users SET email='cashier1@pos.com' WHERE id = 11;
UPDATE users SET email='cashier2@pos.com' WHERE id = 12;
UPDATE users SET email='az@pos.com' WHERE id = 18;
UPDATE users SET email='bob@pos.com' WHERE id = 22;
UPDATE users SET email='agil@pos.com' WHERE id = 13;

ALTER TABLE users
ADD CONSTRAINT uq_email UNIQUE (email);

ALTER TABLE users
ALTER COLUMN email SET NOT NULL;