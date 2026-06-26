-- User 2 (Gustavo Ramírez) is a THERAPIST but has a row in both therapists and patients tables.
-- This corrupts Hibernate's JOINED-inheritance entity loading (L1 cache collision).
-- Appointments 7, 16, 20, 71 have user_id=2 (a therapist) in the patient FK column.
-- None of them have related plan_sessions or subobjective_entries.

DELETE FROM appointment WHERE user_id = 2;

DELETE FROM patients WHERE id = 2;
