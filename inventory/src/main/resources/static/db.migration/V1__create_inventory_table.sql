CREATE TABLE inventory (
                           id BIGSERIAL PRIMARY KEY,
                           product_id BIGINT NOT NULL UNIQUE,
                           product_name VARCHAR(255) NOT NULL,
                           available_stock INTEGER NOT NULL,
                           reserved_stock INTEGER NOT NULL DEFAULT 0
);
