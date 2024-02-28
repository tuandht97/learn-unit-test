create table carts (
id BIGSERIAL PRIMARY KEY NOT NULL,
user_id INT NOT NULL,
total_price numeric(15, 2),
created timestamp,
updated timestamp
);
