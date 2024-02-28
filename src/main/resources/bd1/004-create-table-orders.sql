CREATE TABLE orders (
id UUID PRIMARY KEY NOT NULL,
user_id INT,
total_amount NUMERIC(19,2),
order_status TEXT,
created TIMESTAMP,
updated TIMESTAMP
);