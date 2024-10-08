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