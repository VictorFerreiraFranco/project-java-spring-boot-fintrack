-- create database fintrack;

drop table if exists users;
create table users
(
    id       uuid         not null primary key,
    name     varchar(255) not null,
    email    varchar(255) not null,
    password text         not null,
    role     varchar(255) not null
);

drop table if exists refresh_tokens;
CREATE TABLE refresh_tokens
(
    id          uuid         not null primary key,
    token       varchar(255) not null unique,
    expiry_date timestamp    not null,
    user_id     uuid         not null,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

drop table if exists workspaces;
CREATE TABLE workspaces
(
    id         uuid         not null primary key,
    name       varchar(255) not null,
    type       varchar(255) not null,
    created_by uuid         not null,
    created_at timestamp    not null,
    deleted_by uuid         null,
    deleted_at timestamp    null,
    FOREIGN KEY (created_by) REFERENCES users (id),
    FOREIGN KEY (deleted_by) REFERENCES users (id)
);