ALTER TABLE plan_session
    ADD COLUMN IF NOT EXISTS transcript  TEXT,
    ADD COLUMN IF NOT EXISTS ai_summary  TEXT;
