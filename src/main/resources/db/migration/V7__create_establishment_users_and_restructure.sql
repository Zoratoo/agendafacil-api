CREATE TABLE establishment_users (
     id               UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
     establishment_id UUID        NOT NULL REFERENCES establishments(id),
     user_id          UUID        NOT NULL REFERENCES users(id),
     role             VARCHAR(30) NOT NULL,
     created_at       TIMESTAMP   NOT NULL DEFAULT NOW(),
     UNIQUE (establishment_id, user_id)
);

ALTER TABLE working_hours DROP COLUMN professional_id;
ALTER TABLE working_hours ADD COLUMN establishment_id UUID NOT NULL REFERENCES establishments(id);
ALTER TABLE working_hours ADD COLUMN user_id UUID NOT NULL REFERENCES users(id);

ALTER TABLE blocked_slots DROP COLUMN professional_id;
ALTER TABLE blocked_slots ADD COLUMN establishment_id UUID NOT NULL REFERENCES establishments(id);
ALTER TABLE blocked_slots ADD COLUMN user_id UUID NOT NULL REFERENCES users(id);