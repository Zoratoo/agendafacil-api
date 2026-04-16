CREATE TABLE bookings (
      id               UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
      establishment_id UUID         NOT NULL REFERENCES establishments(id),
      service_id       UUID         NOT NULL REFERENCES services_offered(id),
      professional_id  UUID         NOT NULL REFERENCES users(id),
      client_id        UUID         NOT NULL REFERENCES users(id),
      booking_date     DATE         NOT NULL,
      start_time       TIME         NOT NULL,
      end_time         TIME         NOT NULL,
      status           VARCHAR(20)  NOT NULL DEFAULT 'CONFIRMED',
      created_at       TIMESTAMP    NOT NULL DEFAULT NOW(),
      updated_at       TIMESTAMP    NOT NULL DEFAULT NOW()
);