create table life_disk.users_token
(
    id         bigint auto_increment
        primary key,
    auth_token varchar(255) not null,
    login      varchar(255) not null
);