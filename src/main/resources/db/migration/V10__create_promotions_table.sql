DO $$ BEGIN
CREATE TYPE promo_type AS ENUM ('PERCENTAGE', 'FIXED_AMOUNT');
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

CREATE TABLE promotions (
            id          BIGSERIAL PRIMARY KEY,
            name        VARCHAR(200) NOT NULL,
            type        promo_type NOT NULL,
            value       NUMERIC(10,4) NOT NULL CHECK (value > 0),
            starts_at   TIMESTAMPTZ NOT NULL,
            ends_at     TIMESTAMPTZ NOT NULL,
            active      BOOLEAN NOT NULL DEFAULT TRUE,
            created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
            CHECK (ends_at > starts_at)
);

CREATE TABLE promotion_products (
            promotion_id BIGINT NOT NULL REFERENCES promotions(id) ON DELETE CASCADE,
            product_id   BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
            PRIMARY KEY (promotion_id, product_id)
);

CREATE INDEX idx_promotions_active_dates ON promotions(active, starts_at, ends_at);