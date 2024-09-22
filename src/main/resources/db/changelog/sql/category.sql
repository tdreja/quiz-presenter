-- liquibase formatted sql
-- changeset tdreja:001-create-category-table
create table
    category (
        id INTEGER not null,
        name TEXT not null,
        quiz_id INTEGER not null,
        locale TEXT not null,
        primary key (id)
    );

create table
    category_questions (
        category_id INTEGER not null,
        question_id INTEGER not null,
        primary key (category_id, question_id)
    );