ALTER TABLE users
ADD COLUMN keycloak_id UUID UNIQUE,
DROP COLUMN password_hash;


