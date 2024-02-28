CREATE TABLE order_line_item (
id BIGSERIAL PRIMARY KEY NOT NULL,
order_id UUID,
product_id INT,
quantity INT,
price NUMERIC
);