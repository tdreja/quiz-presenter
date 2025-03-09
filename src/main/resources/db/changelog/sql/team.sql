-- liquibase formatted sql
-- changeset tdreja:001-create-team-table
create table
    team
(
    id           INTEGER not null,
    order_number INTEGER not null,
    color        TEXT    not null,
    points       INTEGER not null,
    game_id      INTEGER not null,
    primary key (id)
);
-- changeset tdreja:002-alter-team-table
alter table team drop column order_number;
alter table team add column next_turn_number INTEGER not null;
alter table team add column gamepad_requested INTEGER not null default 0;
alter table team add column gamepad_id TEXT null;