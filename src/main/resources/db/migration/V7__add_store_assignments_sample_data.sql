INSERT INTO store_assignments (store_id, user_id)
SELECT s.id, u.id FROM stores s, users u
WHERE s.name = 'Main Store' AND u.id = 11
    ON CONFLICT DO NOTHING;

INSERT INTO store_assignments (store_id, user_id)
SELECT s.id, u.id FROM stores s, users u
WHERE s.name = 'Main Store' AND u.id = 12
    ON CONFLICT DO NOTHING;