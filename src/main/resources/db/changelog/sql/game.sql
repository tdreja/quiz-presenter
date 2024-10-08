-- liquibase formatted sql
-- changeset tdreja:001-create-game-table
create table
    game
(
    id                  INTEGER not null,
    quiz_id             INTEGER not null,
    locale              TEXT    not null,
    game_mode           TEXT    not null,
    interactive         INTEGER not null,
    active_team_id      INTEGER null,
    active_player_id    INTEGER null,
    current_question_id INTEGER null,
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