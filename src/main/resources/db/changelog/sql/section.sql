-- liquibase formatted sql
-- changeset tdreja:001-create-section-table
create table
    section (
        id INTEGER not null,
        name TEXT not null,
        quiz_id INTEGER not null,
        locale TEXT not null,
        primary key (id)
    );

create table
    questions_in_section (
        section_id INTEGER not null,
        question_id INTEGER not null,
        primary key (section_id, question_id)
    );