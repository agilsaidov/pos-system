
-- ================
-- ENUMS
-- ================
DO $$ BEGIN
CREATE TYPE role_name AS ENUM ('ADMIN', 'MANAGER', 'CASHIER');
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

DO $$ BEGIN
CREATE TYPE stock_movement_type AS ENUM ('IN', 'OUT', 'ADJUST', 'SALE', 'RETURN');
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

DO $$ BEGIN
CREATE TYPE sale_status AS ENUM ('COMPLETED', 'CANCELLED');
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

DO $$ BEGIN
CREATE TYPE payment_method AS ENUM ('CASH', 'CARD', 'MIXED');
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

-- ================
-- USERS & ROLES
-- ================
CREATE TABLE IF NOT EXISTS users (
        id           BIGSERIAL PRIMARY KEY,
        username     VARCHAR(100) NOT NULL UNIQUE,
        password_hash TEXT        NOT NULL,
        full_name    VARCHAR(200) NOT NULL,
        enabled      BOOLEAN      NOT NULL DEFAULT TRUE,
        created_at   TIMESTAMPTZ  NOT NULL DEFAULT now()
    );

CREATE TABLE IF NOT EXISTS roles (
        id   SMALLSERIAL PRIMARY KEY,
        name role_name NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS user_roles (
        user_id BIGINT  NOT NULL REFERENCES users(id) ON DELETE CASCADE,
        role_id SMALLINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
        PRIMARY KEY (user_id, role_id)
    );

-- ================
-- STORES
-- ================
CREATE TABLE IF NOT EXISTS stores (
        id   BIGSERIAL PRIMARY KEY,
        name VARCHAR(200) NOT NULL UNIQUE
    );

-- ================
-- CATALOG
-- ================
CREATE TABLE IF NOT EXISTS categories (
        id   BIGSERIAL PRIMARY KEY,
        name VARCHAR(200) NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS products (
        id         BIGSERIAL PRIMARY KEY,
        barcode    VARCHAR(64)  NOT NULL UNIQUE,
        name       VARCHAR(255) NOT NULL,
        price      NUMERIC(12,2) NOT NULL CHECK (price >= 0),
        cost       NUMERIC(12,2) NOT NULL CHECK (cost >= 0),
        tax_rate   NUMERIC(5,4)  NOT NULL DEFAULT 0 CHECK (tax_rate >= 0), -- e.g. 0.18
        active     BOOLEAN       NOT NULL DEFAULT TRUE,
        created_at TIMESTAMPTZ   NOT NULL DEFAULT now()
    );

CREATE TABLE IF NOT EXISTS product_categories (
        product_id  BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
        category_id BIGINT NOT NULL REFERENCES categories(id) ON DELETE CASCADE,
        PRIMARY KEY (product_id, category_id)
    );

-- ================
-- INVENTORY
-- ================
CREATE TABLE IF NOT EXISTS inventory (
        store_id   BIGINT NOT NULL REFERENCES stores(id) ON DELETE CASCADE,
        product_id BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
        quantity   INTEGER NOT NULL DEFAULT 0 CHECK (quantity >= 0),
        updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
        PRIMARY KEY (store_id, product_id)
    );

CREATE TABLE IF NOT EXISTS stock_movements (
        id          BIGSERIAL PRIMARY KEY,
        store_id    BIGINT NOT NULL REFERENCES stores(id) ON DELETE CASCADE,
        product_id  BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
        type        stock_movement_type NOT NULL,
        qty_delta   INTEGER NOT NULL CHECK (qty_delta <> 0),
        qty_before  INTEGER NOT NULL CHECK (qty_before >= 0),
        qty_after   INTEGER NOT NULL CHECK (qty_after >= 0),
        reason      VARCHAR(500),
        created_by  BIGINT NOT NULL REFERENCES users(id),
        created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
        CHECK (qty_after = qty_before + qty_delta)
    );

-- ================
-- SALES
-- ================
CREATE TABLE IF NOT EXISTS sales (
        id             BIGSERIAL PRIMARY KEY,
        store_id       BIGINT NOT NULL REFERENCES stores(id) ON DELETE RESTRICT,
        cashier_id     BIGINT NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
        receipt_no     VARCHAR(50) NOT NULL UNIQUE,
        subtotal       NUMERIC(12,2) NOT NULL CHECK (subtotal >= 0),
        tax_total      NUMERIC(12,2) NOT NULL CHECK (tax_total >= 0),
        discount_total NUMERIC(12,2) NOT NULL DEFAULT 0 CHECK (discount_total >= 0),
        total          NUMERIC(12,2) NOT NULL CHECK (total >= 0),
        status         sale_status NOT NULL DEFAULT 'COMPLETED',
        created_at     TIMESTAMPTZ NOT NULL DEFAULT now()
    );

CREATE TABLE IF NOT EXISTS sale_items (
        id                 BIGSERIAL PRIMARY KEY,
        sale_id            BIGINT NOT NULL REFERENCES sales(id) ON DELETE CASCADE,
        product_id         BIGINT NOT NULL REFERENCES products(id) ON DELETE RESTRICT,
        quantity           INTEGER NOT NULL CHECK (quantity > 0),
        unit_price_snapshot NUMERIC(12,2) NOT NULL CHECK (unit_price_snapshot >= 0),
        tax_rate_snapshot   NUMERIC(5,4)  NOT NULL DEFAULT 0 CHECK (tax_rate_snapshot >= 0),
        line_total          NUMERIC(12,2) NOT NULL CHECK (line_total >= 0)
    );

CREATE TABLE IF NOT EXISTS payments (
                                        id         BIGSERIAL PRIMARY KEY,
                                        sale_id    BIGINT NOT NULL REFERENCES sales(id) ON DELETE CASCADE,
    method     payment_method NOT NULL,
    amount     NUMERIC(12,2) NOT NULL CHECK (amount > 0),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
    );

-- ================
-- INDEXES (reports + speed)
-- ================
CREATE INDEX IF NOT EXISTS idx_sales_store_created_at
    ON sales(store_id, created_at);

CREATE INDEX IF NOT EXISTS idx_sales_cashier_created_at
    ON sales(cashier_id, created_at);

CREATE INDEX IF NOT EXISTS idx_stock_movements_product_created_at
    ON stock_movements(product_id, created_at);

CREATE INDEX IF NOT EXISTS idx_sale_items_sale_id
    ON sale_items(sale_id);

-- ================
-- SEED BASE ROLES + DEFAULT STORE (OPTIONAL)
-- ================
INSERT INTO roles(name) VALUES ('ADMIN'), ('MANAGER'), ('CASHIER')
    ON CONFLICT (name) DO NOTHING;

INSERT INTO stores(name) VALUES ('Main Store')
    ON CONFLICT (name) DO NOTHING;