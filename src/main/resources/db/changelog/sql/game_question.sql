-- liquibase formatted sql
-- changeset tdreja:001-create-game-question-table
create table
    game_question (
        id INTEGER not null,
        points INTEGER not null,
        section_id INTEGER not null,
        question_id INTEGER not null,
        answered_by_team_id INTEGER null,
        primary key (id)
    );