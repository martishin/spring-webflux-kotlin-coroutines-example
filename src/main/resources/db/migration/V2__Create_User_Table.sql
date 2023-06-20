CREATE TABLE application.user
(
    id         SERIAL       NOT NULL PRIMARY KEY,
    email      VARCHAR(255) NOT NULL UNIQUE,
    name       VARCHAR(255) NOT NULL,
    age        INT          NOT NULL,
    company_id BIGINT       NOT NULL REFERENCES application.company (id) ON DELETE CASCADE
);
