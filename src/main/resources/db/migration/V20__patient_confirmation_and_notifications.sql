alter table appointment
    add column patient_confirmed_at timestamp;

alter table companies
    add column cancellation_window_hours integer not null default 24;

create table if not exists notification
(
    id                     serial primary key,
    recipient_user_id      integer      not null,
    type                   varchar(40)  not null,
    title                  varchar(120) not null,
    body                   varchar(500) not null,
    related_appointment_id integer,
    read_at                timestamp,
    created_at             timestamp    not null default now(),
    foreign key (recipient_user_id) references users (id) on delete cascade,
    foreign key (related_appointment_id) references appointment (id) on delete cascade
);

alter table notification
    owner to lira;

create index idx_notification_recipient on notification (recipient_user_id);
