CREATE TABLE services_offered (
      id               UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
      establishment_id UUID         NOT NULL REFERENCES establishments(id),
      name             VARCHAR(100) NOT NULL,
      description      TEXT,
      duration_minutes INTEGER      NOT NULL,
      price            NUMERIC(10,2),
      active           BOOLEAN      NOT NULL DEFAULT TRUE,
      created_at       TIMESTAMP    NOT NULL DEFAULT NOW(),
      updated_at       TIMESTAMP    NOT NULL DEFAULT NOW()
);