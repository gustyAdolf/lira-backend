-- plan-session-history: unified session record for clinical work

CREATE TABLE plan_session (
    id              SERIAL PRIMARY KEY,
    plan_id         INT NOT NULL REFERENCES progress_plan(id) ON DELETE CASCADE,
    therapist_id    INT NOT NULL REFERENCES therapists(id) ON DELETE SET NULL,
    appointment_id  INT REFERENCES appointment(id) ON DELETE SET NULL,
    session_date    TIMESTAMP NOT NULL DEFAULT NOW(),
    notes           TEXT,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW()
);

ALTER TABLE subobjective_entries
    ADD COLUMN plan_session_id INT REFERENCES plan_session(id) ON DELETE SET NULL;
