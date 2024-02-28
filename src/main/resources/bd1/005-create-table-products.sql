CREATE TABLE products (
id BIGSERIAL PRIMARY KEY NOT NULL,
name TEXT,
description TEXT,
price NUMERIC(19,2),
quantity INT,
category_id INT,
created TIMESTAMP,
updated TIMESTAMP
);