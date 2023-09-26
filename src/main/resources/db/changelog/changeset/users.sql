create table life_disk.users
(
    id       bigint auto_increment not null primary key,
    username varchar(30) not null,
    password varchar(100) not null,
    constraint UK_r43af9ap4edm43mmtq01oddj6
        unique (username)
);