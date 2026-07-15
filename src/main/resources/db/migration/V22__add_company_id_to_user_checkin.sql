alter table user_checkin
    add column company_id integer references companies (id);
