-- patient-owned task journal: fully separate from progress_plan/subobjectives,
-- never written to by the official clinical progress flow

CREATE TABLE patient_task (
    id           SERIAL PRIMARY KEY,
    patient_id   INT NOT NULL REFERENCES patients(id) ON DELETE CASCADE,
    title        VARCHAR(255) NOT NULL,
    description  TEXT,
    target_value INT,
    created_at   TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE patient_task_entry (
    id               SERIAL PRIMARY KEY,
    patient_id       INT NOT NULL REFERENCES patients(id) ON DELETE CASCADE,
    subobjective_id  INT REFERENCES subobjectives(id) ON DELETE CASCADE,
    patient_task_id  INT REFERENCES patient_task(id) ON DELETE CASCADE,
    note             TEXT,
    emotion_entry_id INT REFERENCES emotion_entry(id) ON DELETE SET NULL,
    created_at       TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT patient_task_entry_target_xor CHECK (
        (subobjective_id IS NOT NULL) <> (patient_task_id IS NOT NULL)
    )
);

alter table patient_task owner to lira;
alter table patient_task_entry owner to lira;
