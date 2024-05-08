CREATE TABLE product
(
    id    BIGSERIAL PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    price NUMERIC,
    quantity INTEGER
);