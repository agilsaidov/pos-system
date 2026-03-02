-- ================
-- USERS
-- ================
INSERT INTO users (username, full_name, enabled, keycloak_id) VALUES
      ('admin',   'Admin User',      TRUE, gen_random_uuid()),
      ('manager1','Sara Johnson',    TRUE, gen_random_uuid()),
      ('cashier1','John Doe',        TRUE, gen_random_uuid()),
      ('cashier2','Emily Clark',     TRUE, gen_random_uuid())
    ON CONFLICT (username) DO NOTHING;

-- ================
-- USER ROLES
-- ================
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username = 'admin'    AND r.name = 'ADMIN'
    ON CONFLICT DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username = 'manager1' AND r.name = 'MANAGER'
    ON CONFLICT DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username = 'cashier1' AND r.name = 'CASHIER'
    ON CONFLICT DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username = 'cashier2' AND r.name = 'CASHIER'
    ON CONFLICT DO NOTHING;

-- ================
-- STORES
-- ================
INSERT INTO stores (name) VALUES
      ('Branch - Downtown'),
      ('Branch - Mall')
    ON CONFLICT (name) DO NOTHING;

-- ================
-- CATEGORIES
-- ================
INSERT INTO categories (name) VALUES
      ('Beverages'),
      ('Snacks'),
      ('Dairy'),
      ('Bakery'),
      ('Electronics')
    ON CONFLICT (name) DO NOTHING;

-- ================
-- PRODUCTS
-- ================
INSERT INTO products (barcode, name, price, cost, tax_rate, active) VALUES
    ('1000000001', 'Coca-Cola 500ml',     1.50,  0.80, 0.10, TRUE),
    ('1000000002', 'Pepsi 500ml',         1.50,  0.80, 0.10, TRUE),
    ('1000000003', 'Water 1L',            0.75,  0.30, 0.10, TRUE),
    ('1000000004', 'Lays Chips 100g',     2.00,  1.00, 0.10, TRUE),
    ('1000000005', 'Snickers Bar',        1.20,  0.60, 0.10, TRUE),
    ('1000000006', 'Milk 1L',             1.80,  1.00, 0.08, TRUE),
    ('1000000007', 'Cheese 200g',         3.50,  2.00, 0.08, TRUE),
    ('1000000008', 'Croissant',           1.00,  0.40, 0.08, TRUE),
    ('1000000009', 'Sourdough Bread',     2.50,  1.20, 0.08, TRUE),
    ('1000000010', 'USB-C Cable 1m',      8.99,  4.00, 0.18, TRUE)
    ON CONFLICT (barcode) DO NOTHING;

-- ================
-- PRODUCT CATEGORIES
-- ================
INSERT INTO product_categories (product_id, category_id)
SELECT p.id, c.id FROM products p, categories c
WHERE (p.barcode IN ('1000000001','1000000002','1000000003') AND c.name = 'Beverages')
   OR (p.barcode IN ('1000000004','1000000005')              AND c.name = 'Snacks')
   OR (p.barcode IN ('1000000006','1000000007')              AND c.name = 'Dairy')
   OR (p.barcode IN ('1000000008','1000000009')              AND c.name = 'Bakery')
   OR (p.barcode = '1000000010'                              AND c.name = 'Electronics')
    ON CONFLICT DO NOTHING;

-- ================
-- INVENTORY (Main Store + Downtown)
-- ================
INSERT INTO inventory (store_id, product_id, quantity)
SELECT s.id, p.id,
       CASE p.barcode
           WHEN '1000000001' THEN 100
           WHEN '1000000002' THEN 80
           WHEN '1000000003' THEN 200
           WHEN '1000000004' THEN 60
           WHEN '1000000005' THEN 90
           WHEN '1000000006' THEN 50
           WHEN '1000000007' THEN 30
           WHEN '1000000008' THEN 40
           WHEN '1000000009' THEN 25
           WHEN '1000000010' THEN 15
           END
FROM stores s, products p
WHERE s.name = 'Main Store'
    ON CONFLICT DO NOTHING;

INSERT INTO inventory (store_id, product_id, quantity)
SELECT s.id, p.id,
       CASE p.barcode
           WHEN '1000000001' THEN 50
           WHEN '1000000002' THEN 40
           WHEN '1000000003' THEN 100
           WHEN '1000000004' THEN 30
           WHEN '1000000005' THEN 45
           WHEN '1000000006' THEN 20
           WHEN '1000000007' THEN 15
           WHEN '1000000008' THEN 20
           WHEN '1000000009' THEN 10
           WHEN '1000000010' THEN 5
           END
FROM stores s, products p
WHERE s.name = 'Branch - Downtown'
    ON CONFLICT DO NOTHING;

-- ================
-- STOCK MOVEMENTS
-- ================
INSERT INTO stock_movements (store_id, product_id, type, qty_delta, qty_before, qty_after, reason, created_by)
SELECT s.id, p.id, 'IN', 100, 0, 100, 'Initial stock', u.id
FROM stores s, products p, users u
WHERE s.name = 'Main Store' AND p.barcode = '1000000001' AND u.username = 'admin';

INSERT INTO stock_movements (store_id, product_id, type, qty_delta, qty_before, qty_after, reason, created_by)
SELECT s.id, p.id, 'SALE', -3, 100, 97, NULL, u.id
FROM stores s, products p, users u
WHERE s.name = 'Main Store' AND p.barcode = '1000000001' AND u.username = 'cashier1';

-- ================
-- SALES
-- ================
INSERT INTO sales (store_id, cashier_id, receipt_no, subtotal, tax_total, discount_total, total, status)
SELECT s.id, u.id, 'RCP-0001', 4.50, 0.45, 0.00, 4.95, 'COMPLETED'
FROM stores s, users u
WHERE s.name = 'Main Store' AND u.username = 'cashier1';

INSERT INTO sales (store_id, cashier_id, receipt_no, subtotal, tax_total, discount_total, total, status)
SELECT s.id, u.id, 'RCP-0002', 7.20, 0.72, 0.50, 7.42, 'COMPLETED'
FROM stores s, users u
WHERE s.name = 'Main Store' AND u.username = 'cashier2';

-- ================
-- SALE ITEMS
-- ================
INSERT INTO sale_items (sale_id, product_id, quantity, unit_price_snapshot, tax_rate_snapshot, line_total)
SELECT sa.id, p.id, 2, 1.50, 0.10, 3.00
FROM sales sa, products p
WHERE sa.receipt_no = 'RCP-0001' AND p.barcode = '1000000001';

INSERT INTO sale_items (sale_id, product_id, quantity, unit_price_snapshot, tax_rate_snapshot, line_total)
SELECT sa.id, p.id, 1, 1.50, 0.10, 1.50
FROM sales sa, products p
WHERE sa.receipt_no = 'RCP-0001' AND p.barcode = '1000000003';

INSERT INTO sale_items (sale_id, product_id, quantity, unit_price_snapshot, tax_rate_snapshot, line_total)
SELECT sa.id, p.id, 2, 2.00, 0.10, 4.00
FROM sales sa, products p
WHERE sa.receipt_no = 'RCP-0002' AND p.barcode = '1000000004';

INSERT INTO sale_items (sale_id, product_id, quantity, unit_price_snapshot, tax_rate_snapshot, line_total)
SELECT sa.id, p.id, 1, 3.20, 0.10, 3.20
FROM sales sa, products p
WHERE sa.receipt_no = 'RCP-0002' AND p.barcode = '1000000005';

-- ================
-- PAYMENTS
-- ================
INSERT INTO payments (sale_id, method, amount)
SELECT id, 'CASH', 4.95 FROM sales WHERE receipt_no = 'RCP-0001';

INSERT INTO payments (sale_id, method, amount)
SELECT id, 'CARD', 7.42 FROM sales WHERE receipt_no = 'RCP-0002';