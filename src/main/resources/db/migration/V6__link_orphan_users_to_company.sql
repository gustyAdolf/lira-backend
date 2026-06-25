-- Link orphaned test users to company 14 (Clínica phobos)
-- Patients: 5 (prueba), 11 (hbvihi), 18 (Nuevo del movil), 21 (Jaime Perez)
-- Therapists: 22 (Steven), 25 (terapeutaPro)
INSERT INTO user_company (user_id, company_id)
SELECT u.id, 14
FROM (VALUES (5), (11), (18), (21), (22), (25)) AS u(id)
WHERE NOT EXISTS (
    SELECT 1 FROM user_company uc WHERE uc.user_id = u.id AND uc.company_id = 14
);
