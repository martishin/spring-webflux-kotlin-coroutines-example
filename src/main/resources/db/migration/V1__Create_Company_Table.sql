CREATE SCHEMA IF NOT EXISTS application;

CREATE TABLE application.companies
(
    id      SERIAL       NOT NULL PRIMARY KEY,
    name    VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL
);
