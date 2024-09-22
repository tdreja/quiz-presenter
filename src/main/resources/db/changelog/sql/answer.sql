-- liquibase formatted sql
-- changeset tdreja:001-create-answer-table
create table
    answer (
        id INTEGER not null,
        is_correct INTEGER not null,
        answer_text TEXT null,
        multiple_choice_question INTEGER null,
        answer_type TEXT not null,
        locale TEXT not null,
        primary key (id)
    );