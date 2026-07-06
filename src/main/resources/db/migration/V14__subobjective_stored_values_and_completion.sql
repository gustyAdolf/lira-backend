-- Add is_completed flag and stored aggregate counters to subobjectives
ALTER TABLE subobjectives
    ADD COLUMN is_completed        BOOLEAN          NOT NULL DEFAULT FALSE,
    ADD COLUMN current_value       INT              NOT NULL DEFAULT 0,
    ADD COLUMN current_success_count INT            NOT NULL DEFAULT 0,
    ADD COLUMN current_fail_count  INT              NOT NULL DEFAULT 0;

-- Backfill stored counters from existing subobjective_entries
UPDATE subobjectives s
SET current_value        = COALESCE((SELECT COALESCE(SUM(value_increment), 0)
                                      FROM subobjective_entries e
                                      WHERE e.subobjective_id = s.id), 0),
    current_success_count = COALESCE((SELECT COUNT(*)
                                       FROM subobjective_entries e
                                       WHERE e.subobjective_id = s.id AND e.is_success = TRUE), 0),
    current_fail_count   = COALESCE((SELECT COUNT(*)
                                      FROM subobjective_entries e
                                      WHERE e.subobjective_id = s.id AND e.is_success = FALSE), 0);

-- Reset current_progress for QUALITATIVE subobjectives not yet marked completed
-- (old logic computed progress from aciertos/targetSuccess which is no longer valid)
UPDATE subobjectives
SET current_progress = 0.0
WHERE type = 'QUALITATIVE'
  AND is_completed = FALSE;
