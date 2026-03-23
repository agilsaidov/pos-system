ALTER TABLE users
ADD COLUMN first_name VARCHAR(100),
ADD COLUMN last_name VARCHAR(100);

UPDATE users SET
     first_name = SPLIT_PART(full_name, ' ', 1),
     last_name = NULLIF(SPLIT_PART(full_name, ' ', 2), '');

ALTER TABLE users DROP COLUMN full_name;