CREATE TABLE establishments (
    id          UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    owner_id    UUID         NOT NULL REFERENCES users(id),
    name        VARCHAR(100) NOT NULL,
    category    VARCHAR(50)  NOT NULL,
    phone       VARCHAR(20),
    cep         VARCHAR(10),
    address     VARCHAR(150),
    number      VARCHAR(10),
    neighborhood VARCHAR(80),
    city        VARCHAR(80),
    state       VARCHAR(2),
    active      BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP    NOT NULL DEFAULT NOW()
);