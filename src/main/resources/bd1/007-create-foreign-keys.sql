ALTER TABLE IF EXISTS order_line_item
    ADD CONSTRAINT FKbgsq1dd0vi98leb4g7irlei84 FOREIGN KEY (order_id) REFERENCES orders;

ALTER TABLE IF EXISTS order_line_item
    ADD CONSTRAINT FKmj2lnm5vhprlqtxuqgx2fikn4 FOREIGN KEY (product_id) REFERENCES products;

ALTER TABLE IF EXISTS orders
    ADD CONSTRAINT FK32ql8ubntj5uh44ph9659tiih FOREIGN KEY (user_id) REFERENCES users;

ALTER TABLE IF EXISTS products
    ADD CONSTRAINT FK1cf90etcu98x1e6n9aks3tel3 FOREIGN KEY (category_id) REFERENCES category;
