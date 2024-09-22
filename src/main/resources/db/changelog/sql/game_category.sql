-- liquibase formatted sql
-- changeset tdreja:001-create-game-category-table
create table
    game_category (
        id INTEGER not null,
        category_id INTEGER not null,
        game_id INTEGER not null,
        primary key (id)
    );