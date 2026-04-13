CREATE TABLE working_hours (
   id              UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
   professional_id UUID        NOT NULL REFERENCES professionals(id),
   day_of_week     VARCHAR(15) NOT NULL,
   start_time      TIME        NOT NULL,
   end_time        TIME        NOT NULL,
   created_at      TIMESTAMP   NOT NULL DEFAULT NOW(),
   updated_at      TIMESTAMP   NOT NULL DEFAULT NOW(),
   CONSTRAINT valid_hours CHECK (end_time > start_time)
);

CREATE TABLE blocked_slots (
   id              UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
   professional_id UUID        NOT NULL REFERENCES professionals(id),
   blocked_date    DATE        NOT NULL,
   start_time      TIME,
   end_time        TIME,
   reason          VARCHAR(255),
   created_at      TIMESTAMP   NOT NULL DEFAULT NOW()
);