-- liquibase formatted sql
-- changeset tdreja:001-create-question-table
create table
    question (
        id INTEGER not null,
        correct_answer INTEGER not null,
        question_type TEXT not null,
        locale TEXT not null,
        difficulty INTEGER not null,
        question_text TEXT not null,
        primary key (id)
    );