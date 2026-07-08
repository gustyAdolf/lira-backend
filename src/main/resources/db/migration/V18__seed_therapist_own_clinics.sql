-- Ensure the users sequence is ahead of any explicit IDs in seed data
SELECT setval('users_id_seq', GREATEST((SELECT MAX(id) FROM users), nextval('users_id_seq') - 1));

DO $$
DECLARE
    t_id       INTEGER;
    t_name     TEXT;
    new_co_id  INTEGER;
BEGIN
    FOR t_id, t_name IN
        SELECT u.id, u.name
        FROM users u
                 JOIN therapists th ON u.id = th.id
        WHERE NOT EXISTS (
            SELECT 1 FROM user_company uc WHERE uc.user_id = u.id AND uc.is_owner = true
        )
        LOOP
            INSERT INTO users (email, password, name, user_type, release_date)
            VALUES (
                       'clinic_' || t_id || '@lira.internal',
                       '$2a$10$BGXswNTAXDNV36GiPok2SORQyGn484ifTFeack8U5Vtp2qh2.6Bl2',
                       'Consulta de ' || t_name,
                       'COMPANY',
                       NOW()
                   )
            RETURNING id INTO new_co_id;

            INSERT INTO companies (id, cif, company_address)
            VALUES (new_co_id, NULL, NULL);

            INSERT INTO user_company (user_id, company_id, is_owner)
            VALUES (t_id, new_co_id, true);
        END LOOP;
END $$;
