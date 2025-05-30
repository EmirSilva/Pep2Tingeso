CREATE DATABASE pricedb;

GRANT ALL PRIVILEGES ON DATABASE pricedb TO postgres;

\c pricedb; -- Conectarse a la base de datos pricedb

CREATE TABLE IF NOT EXISTS price (
    id SERIAL PRIMARY KEY,
    base_price DOUBLE PRECISION NOT NULL,
    max_duration INTEGER NOT NULL,
    max_laps INTEGER NOT NULL
);

INSERT INTO price (base_price, max_duration, max_laps) VALUES
(15000, 30, 10),
(20000, 35, 15),
(25000, 40, 20);
