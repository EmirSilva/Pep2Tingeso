CREATE TABLE IF NOT EXISTS group_discount (
    id SERIAL PRIMARY KEY,
    discount_percentage INTEGER NOT NULL,
    max_group_size INTEGER NOT NULL,
    min_group_size INTEGER NOT NULL
);

INSERT INTO group_discount (discount_percentage, max_group_size, min_group_size) VALUES
(10, 5, 3),
(20, 10, 6),
(30, 15, 11);