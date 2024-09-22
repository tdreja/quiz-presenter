-- liquibase formatted sql
-- changeset tdreja:001-create-player-table
create table
    player (
        id INTEGER not null,
        emoji TEXT not null,
        points INTEGER not null,
        team_id INTEGER not null,
        game_id INTEGER not null,
        primary key (id)
    );