-- liquibase formatted sql
-- changeset tdreja:001-create-quiz-table
create table
    quiz (
        id INTEGER not null,
        locale TEXT not null,
        name TEXT not null,
        author TEXT null,
        primary key (id)
    );