create table if not exists therapist_schedule_exception
(
    id           serial primary key,
    therapist_id integer not null,
    date         date    not null,
    start_time   time,
    end_time     time,
    reason       varchar(255),
    created_at   timestamp not null default now(),
    foreign key (therapist_id) references therapists (id) on delete cascade,
    unique (therapist_id, date, start_time)
);

alter table therapist_schedule_exception owner to lira;
