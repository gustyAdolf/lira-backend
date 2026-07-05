create table if not exists therapist_schedule
(
    therapist_id integer  not null,
    day_of_week  smallint not null,
    start_time   time     not null,
    end_time     time     not null,
    primary key (therapist_id, day_of_week),
    foreign key (therapist_id) references therapists (id) on delete cascade
);

alter table therapist_schedule owner to lira;
