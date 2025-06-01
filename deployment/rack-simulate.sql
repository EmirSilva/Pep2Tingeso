-- rack-simulation.sql

CREATE TABLE IF NOT EXISTS rack_semanal (
    id SERIAL PRIMARY KEY,
    estado VARCHAR(255),
    fecha_hora TIMESTAMP WITHOUT TIME ZONE,
    reservado_por VARCHAR(255),
    reservation_id BIGINT
);