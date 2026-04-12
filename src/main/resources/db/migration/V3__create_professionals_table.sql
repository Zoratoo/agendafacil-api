CREATE TABLE professionals (
   id          UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
   user_id     UUID         NOT NULL UNIQUE REFERENCES users(id),
   specialty   VARCHAR(100),
   bio         TEXT,
   document    VARCHAR(50),
   avatar_url  VARCHAR(255),
   active      BOOLEAN      NOT NULL DEFAULT TRUE,
   created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
   updated_at  TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE establishment_professionals (
     establishment_id UUID NOT NULL REFERENCES establishments(id),
     professional_id  UUID NOT NULL REFERENCES professionals(id),
     PRIMARY KEY (establishment_id, professional_id)
);