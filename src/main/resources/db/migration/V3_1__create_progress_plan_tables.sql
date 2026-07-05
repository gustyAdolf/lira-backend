-- create progress plan related tables missing from initial setup

CREATE TABLE progress_plan (
    id             SERIAL PRIMARY KEY,
    patient_id     INT NOT NULL REFERENCES patients(id) ON DELETE CASCADE,
    therapist_id   INT NOT NULL REFERENCES therapists(id) ON DELETE CASCADE,
    title          VARCHAR(255) NOT NULL,
    description    TEXT,
    total_progress DOUBLE PRECISION NOT NULL DEFAULT 0,
    created_at     TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at     TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE objectives (
    id          SERIAL PRIMARY KEY,
    plan_id     INT NOT NULL REFERENCES progress_plan(id) ON DELETE CASCADE,
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    order_index INT NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE subobjectives (
    id               SERIAL PRIMARY KEY,
    objective_id     INT NOT NULL REFERENCES objectives(id) ON DELETE CASCADE,
    title            VARCHAR(255) NOT NULL,
    description      TEXT,
    type             VARCHAR(20) NOT NULL CHECK (type IN ('QUALITATIVE', 'QUANTITATIVE')),
    target_value     INT,
    target_success   INT,
    target_fail      INT,
    current_progress DOUBLE PRECISION NOT NULL DEFAULT 0,
    created_at       TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE subobjective_entries (
    id              SERIAL PRIMARY KEY,
    subobjective_id INT NOT NULL REFERENCES subobjectives(id) ON DELETE CASCADE,
    therapist_id    INT NOT NULL REFERENCES therapists(id) ON DELETE CASCADE,
    entry_date      TIMESTAMP NOT NULL,
    value_increment INT NOT NULL,
    is_success      BOOLEAN NOT NULL,
    note            TEXT NOT NULL
);
