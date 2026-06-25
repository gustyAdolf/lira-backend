-- appointment-plan-connection: link appointments to treatment plans

ALTER TABLE appointment
    ADD COLUMN progress_plan_id INT REFERENCES progress_plan(id) ON DELETE SET NULL;

ALTER TABLE appointment
    ADD COLUMN appointment_type VARCHAR(20) NOT NULL DEFAULT 'GENERAL'
        CHECK (appointment_type IN ('FOLLOW_UP', 'FIRST_VISIT', 'GENERAL'));

ALTER TABLE appointment
    ADD COLUMN therapist_notes TEXT;

ALTER TABLE progress_plan
    ADD COLUMN mental_disorder_id SMALLINT REFERENCES mental_disorder(id);

ALTER TABLE subobjective_entries
    ADD COLUMN appointment_id INT REFERENCES appointment(id) ON DELETE SET NULL;

ALTER TABLE appointment
    DROP COLUMN mental_disorder_id;
