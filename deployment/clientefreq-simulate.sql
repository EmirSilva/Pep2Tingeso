CREATE TABLE IF NOT EXISTS visit_discount (
    id SERIAL PRIMARY KEY,
    discount_multiplier DECIMAL NOT NULL,
    min_monthly_visits INTEGER NOT NULL
);

INSERT INTO visit_discount (discount_multiplier, min_monthly_visits) VALUES
(0.7, 7),
(0.8, 5),
(0.9, 2);