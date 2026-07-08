ALTER TABLE user_company
    ADD COLUMN is_owner boolean NOT NULL DEFAULT false;

ALTER TABLE appointment
    ADD COLUMN company_id integer REFERENCES users (id);

CREATE INDEX idx_appointment_company ON appointment (company_id);
