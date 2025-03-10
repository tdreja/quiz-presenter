-- liquibase formatted sql
-- changeset tdreja:001-create-game-table
create table
    game
(
    id                  INTEGER not null,
    quiz_id             INTEGER not null,
    locale              TEXT    not null,
    game_mode           TEXT    not null,
    wait_for_team_input INTEGER not null,
    active_team_id      INTEGER null,
    active_player_id    INTEGER null,
    current_question_id INTEGER null,
    game_start          TEXT    not null,
    game_end            TEXT    null,
    primary key (id)
);
-- changeset tdreja:002-create-settings-table
create table
    game_settings
(
    id            INTEGER not null,
    setting_key   TEXT    not null,
    setting_value TEXT    not null,
    game_id       INTEGER not null,
    primary key (id)
);
-- changeset tdreja:003-cleanup-game-table
drop table game_settings;
alter table game
    drop column quiz_id;
alter table game
    drop column game_mode;
alter table game
    drop column wait_for_team_input;
alter table game
    drop column active_player_id;
alter table game
    drop column current_question_id;
alter table game
    drop column game_start;
alter table game
    drop column game_end;
alter table game
    drop column active_team_id;
alter table game
    add column roundCounter INTEGER not null;
alter table game
    add column state TEXT not null default 'TEAM_SETUP';
-- changeset tdreja:004-add-enum-tables
create table game_available_emojis
(
    game_id INTEGER not null,
    emoji   TEXT    not null,
    primary key (game_id, emoji)
);
create table game_available_colors
(
    game_id INTEGER not null,
    color   TEXT    not null,
    primary key (game_id, color)
);