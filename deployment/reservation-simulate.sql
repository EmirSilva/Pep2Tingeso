-- Crear la tabla usuarios si no existe
CREATE TABLE IF NOT EXISTS usuarios (
    id SERIAL PRIMARY KEY,
    date_of_birth DATE,
    email VARCHAR(255),
    monthly_visits INTEGER,
    name VARCHAR(255)
);

-- Crear la tabla karts si no existe
CREATE TABLE IF NOT EXISTS karts (
    id SERIAL PRIMARY KEY,
    code VARCHAR(255),
    is_available BOOLEAN
);

-- Insertar datos en la tabla karts
INSERT INTO karts (id, code, is_available) VALUES
(1, 'K001', true),
(2, 'K002', true),
(3, 'K003', true),
(4, 'K004', true),
(5, 'K005', true),
(6, 'K006', true),
(7, 'K007', true),
(8, 'K008', true),
(9, 'K009', true),
(10, 'K010', true),
(11, 'K011', true),
(12, 'K012', true),
(13, 'K013', true),
(14, 'K014', true),
(15, 'K015', true);

-- Crear la tabla reservations si no existe
CREATE TABLE IF NOT EXISTS reservations (
    id SERIAL PRIMARY KEY,
    duration INTEGER,
    group_size INTEGER,
    is_birthday BOOLEAN,
    num_laps INTEGER,
    reservation_date DATE,
    reservation_time TIME WITHOUT TIME ZONE,
    total_price DOUBLE PRECISION
);

-- Crear la tabla user_kart_assignment si no existe
CREATE TABLE IF NOT EXISTS user_kart_assignment (
    id SERIAL PRIMARY KEY,
    kart_id INTEGER REFERENCES karts(id),
    reservation_id INTEGER REFERENCES reservations(id),
    user_id INTEGER REFERENCES usuarios(id)
);

-- Crear la tabla reservation_user si no existe
CREATE TABLE IF NOT EXISTS reservation_user (
    reservation_id INTEGER REFERENCES reservations(id),
    user_id INTEGER REFERENCES usuarios(id),
    PRIMARY KEY (reservation_id, user_id)
);