-- Crear la tabla holiday_discount si no existe
CREATE TABLE IF NOT EXISTS holiday_discount (
    id SERIAL PRIMARY KEY,
    discount_percentage DOUBLE PRECISION,
    holiday_date DATE
);

-- Insertar los datos de los feriados
INSERT INTO holiday_discount (id, discount_percentage, holiday_date) VALUES
(1, 0.15, '2025-01-01'),
(2, 0.15, '2025-04-18'),
(3, 0.15, '2025-04-19'),
(4, 0.15, '2025-05-10'),
(5, 0.15, '2025-09-18'),
(6, 0.15, '2025-09-19'),
(7, 0.15, '2025-11-01'),
(8, 0.15, '2025-12-25');