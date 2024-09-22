-- liquibase formatted sql
-- changeset tdreja:001-create-game-table
create table
    game (
        id INTEGER not null,
        game_code TEXT not null,
        quiz_id INTEGER not null,
        locale TEXT not null,
        primary key (id)
    );