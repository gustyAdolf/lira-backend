CREATE TABLE initial_assessment (
    id SERIAL PRIMARY KEY,
    appointment_id INT UNIQUE NOT NULL REFERENCES appointment(id) ON DELETE CASCADE,
    chief_complaint TEXT,
    background TEXT,
    session_notes TEXT,
    next_steps TEXT,
    transcript TEXT,
    audio_local_path TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);
