create table life_disk.file_info
(
    id bigint auto_increment not null primary key,
    file_name varchar(100) not null,
    size bigint not null,
    type varchar(255) not null,
    bytes LONGBLOB not null,
    upload_date datetime(6) not null,
    owner varchar(255) not null,
foreign key (owner)references life_disk.users (username)
);