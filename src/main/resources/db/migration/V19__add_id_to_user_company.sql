-- user_company previously used (user_id, company_id) as composite PK.
-- The JPA entity requires a surrogate id column to be queried directly.
ALTER TABLE user_company DROP CONSTRAINT user_company_pkey;
ALTER TABLE user_company ADD COLUMN id SERIAL PRIMARY KEY;
ALTER TABLE user_company ADD CONSTRAINT user_company_unique UNIQUE (user_id, company_id);
