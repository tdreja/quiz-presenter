-- liquibase formatted sql
-- changeset tdreja:001-create-game-table
create table
    game
(
    id                  INTEGER not null,
    quiz_id             INTEGER not null,
    locale              TEXT    not null,
    game_mode           TEXT    not null,
    active_team_id      INTEGER null,
    active_player_id    INTEGER null,
    current_question_id INTEGER null,
    primary key (id)
);