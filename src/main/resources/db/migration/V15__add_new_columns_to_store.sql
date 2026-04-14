ALTER TABLE stores
ADD COLUMN active BOOLEAN NOT NULL DEFAULT true,
ADD COLUMN address VARCHAR(500),
ADD COLUMN city VARCHAR(100),
ADD COLUMN phone VARCHAR(50),
ADD COLUMN opened_at TIMESTAMPTZ,
ADD COLUMN created_at TIMESTAMPTZ NOT NULL DEFAULT now();

UPDATE stores
SET address = 'Main Street 3, Iceriseher',
    city = 'Baku',
    phone = '+994-000-111-22-33',
    opened_at = now()
WHERE id = 1;

UPDATE stores
SET address = 'Corner Street 1, Akhmedli',
    city = 'Baku',
    phone = '+994-000-111-22-33',
    opened_at = now()
WHERE id = 6;

UPDATE stores
SET address = 'Center, 28 May',
    city = 'Baku',
    phone = '+994-000-111-22-33',
    opened_at = now()
WHERE id = 7;

ALTER TABLE stores
ALTER COLUMN address SET NOT NULL,
ALTER COLUMN city SET NOT NULL,
ALTER COLUMN phone SET NOT NULL,
ALTER COLUMN opened_at SET NOT NULL;