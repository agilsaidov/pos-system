CREATE TABLE store_assignments (
     store_id BIGINT NOT NULL REFERENCES stores(id) ON DELETE CASCADE,
     user_id  BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
     PRIMARY KEY (store_id, user_id)
);

CREATE INDEX idx_store_assignments_user_id ON store_assignments(user_id);