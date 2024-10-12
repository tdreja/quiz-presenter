-- liquibase formatted sql
-- changeset tdreja:001-create-game-section-table
create table
    game_section (
        id INTEGER not null,
        section_id INTEGER not null,
        game_id INTEGER not null,
        complete INTEGER not null,
        name TEXT not null,
        primary key (id)
    );