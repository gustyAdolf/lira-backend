-- Ingresar antes con el usuario lira, NO crearlos con el usuario admin
create table if not exists users
(
    id               serial,
    email            varchar(80)        not null,
    password         varchar(80)        not null,
    name             varchar(80)        not null,
    profile_img_path varchar(80),
    telephone        varchar(80),
    address          varchar(80),
    user_type        varchar(10)        not null,
    release_date     date default now() not null,
    primary key (id),
    unique (email)
);

alter table users
    owner to lira;

create table if not exists mental_disorder
(
    id              smallint    not null,
    mental_disorder varchar(80) not null,
    primary key (id)
);

alter table mental_disorder
    owner to lira;

create table if not exists report_type
(
    id              smallint    not null,
    report_type varchar(80) not null,
    primary key (id)
);

alter table report_type
    owner to lira;

create table if not exists user_disorder
(
    user_id            integer  not null,
    mental_disorder_id smallint not null,
    primary key (user_id, mental_disorder_id),
    foreign key (user_id) references users,
    foreign key (mental_disorder_id) references mental_disorder
);

alter table user_disorder
    owner to lira;

create table if not exists reports
(
    id                 smallint    not null,
    therapist_id       integer not null,
    user_id            integer  not null,
    report_type_id     integer not null,
    data_report    text not null,
    created_at       date     not null,
    primary key (id),
    foreign key (therapist_id) references users
        on delete cascade,
    foreign key (user_id) references users
        on delete cascade,
    foreign key (report_type_id) references report_type
        on delete cascade
);

alter table reports
    owner to lira;

create table if not exists session
(
    id                 serial,
    session_date       date     not null,
    activation_level   integer  not null,
    exposure_level     integer  not null,
    user_id            integer  not null,
    mental_disorder_id smallint not null,
    progress           smallint not null,
    therapist_notes    text,
    user_notes         text,
    primary key (id),
    foreign key (user_id) references users,
    foreign key (mental_disorder_id) references mental_disorder
);

alter table session
    owner to lira;

create table if not exists appointment
(
    id                   serial,
    therapist_id         integer,
    user_id              integer,
    mental_disorder_id   smallint,
    appointment_date     timestamp,
    description          varchar(255),
    appointment_duration integer,
    cost                 numeric(10, 2),
    status               varchar(20) default 'PENDING'::character varying not null,
    primary key (id),
    foreign key (therapist_id) references users
        on delete cascade,
    foreign key (user_id) references users
        on delete cascade,
    foreign key (mental_disorder_id) references mental_disorder
);

alter table appointment
    owner to lira;

create table if not exists user_company
(
    user_id    integer not null,
    company_id integer not null,
    primary key (user_id, company_id),
    foreign key (user_id) references users
);

alter table user_company
    owner to lira;

create table if not exists therapists
(
    id             integer not null,
    license_number varchar(12),
    constraint therapist_pkey
        primary key (id),
    constraint therapist_id_fkey
        foreign key (id) references users
);

alter table therapists
    owner to lira;

create table if not exists patients
(
    id        integer not null,
    birthdate date,
    gender    varchar(10),
    primary key (id),
    foreign key (id) references users
);

alter table patients
    owner to lira;

create table if not exists companies
(
    id              integer not null,
    cif             varchar(12),
    company_address varchar(80),
    primary key (id),
    foreign key (id) references users
);

alter table companies
    owner to lira;

CREATE SEQUENCE IF NOT EXISTS therapist_checkin_id_seq START WITH 1 INCREMENT BY 1;

create table if not exists user_checkin
(
    id            integer default nextval('therapist_checkin_id_seq'::regclass) not null,
    user_id       integer                                                       not null,
    checkin_time  timestamp                                                     not null,
    checkout_time timestamp,
    total_hours   numeric(5, 2) generated always as (
        CASE
            WHEN (checkout_time IS NOT NULL) THEN (EXTRACT(epoch FROM (checkout_time - checkin_time)) / (3600)::numeric)
            ELSE NULL::numeric
            END) stored,
    constraint therapist_checkin_pkey
        primary key (id),
    constraint fk_therapist
        foreign key (user_id) references users
);

alter table user_checkin
    owner to lira;

create table if not exists emotion_entry
(
    id             serial,
    user_id        integer     not null,
    first_emotion  varchar(16) not null,
    second_emotion varchar(16),
    third_emotion  varchar(16),
    intensity      integer,
    notes          varchar(255),
    created_at     timestamp default CURRENT_TIMESTAMP,
    primary key (id),
    foreign key (user_id) references users
        on delete cascade
);

alter table emotion_entry
    owner to lira;

