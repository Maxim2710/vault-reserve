CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
        CONSTRAINT fk_order
            FOREIGN KEY(order_id)
                REFERENCES orders(id)
);
