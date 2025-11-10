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
    token       VARCHAR(255) NOT NULL UNIQUE,
    expiry_date TIMESTAMP    NOT NULL,
    user_id     uuid       NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);