create table cart_line_item (
id BIGSERIAL PRIMARY KEY NOT NULL,
cart_index INT,
cart_line_price NUMERIC(15, 2),
cart_line_quantity INT NOT NULL,
cart_id INT NOT NULL,
product_id INT NOT NULL
);