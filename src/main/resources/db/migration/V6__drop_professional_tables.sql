ALTER TABLE working_hours DROP CONSTRAINT working_hours_professional_id_fkey;
ALTER TABLE blocked_slots DROP CONSTRAINT blocked_slots_professional_id_fkey;

DROP TABLE IF EXISTS establishment_professionals;
DROP TABLE IF EXISTS professionals;