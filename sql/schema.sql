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

drop table if exists workspace_invites;
CREATE TABLE workspace_invites
(
    id           uuid         not null primary key,
    from_user_id uuid         not null,
    to_user_id   uuid         not null,
    workspace_id uuid         not null,
    status       varchar(255) not null,
    created_by   uuid         not null,
    created_at   timestamp    not null,
    FOREIGN KEY (from_user_id) REFERENCES users (id),
    FOREIGN KEY (to_user_id) REFERENCES users (id),
    FOREIGN KEY (workspace_id) REFERENCES workspaces (id),
    FOREIGN KEY (created_by) REFERENCES users (id)
);


drop table if exists workspace_members;
CREATE TABLE workspace_members
(
    id           uuid         not null primary key,
    workspace_id uuid         not null,
    user_id      uuid         not null,
    role         varchar(255) not null,
    created_by   uuid         not null,
    created_at   timestamp    not null,
    deleted_by   uuid         null,
    deleted_at   timestamp    null,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (workspace_id) REFERENCES workspaces (id),
    FOREIGN KEY (created_by) REFERENCES users (id),
    FOREIGN KEY (deleted_by) REFERENCES users (id)
);