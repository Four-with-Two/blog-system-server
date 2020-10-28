create table blog
(
    id           int auto_increment
        primary key,
    author       int          null,
    essay        text         null,
    bio          varchar(256) null,
    release_time date         null,
    title        varchar(20)  null
);

create table user
(
    id            int auto_increment
        primary key,
    user_name     varchar(20)  null,
    user_password varchar(50)  null,
    nick_name     varchar(20)  null,
    mail          varchar(50)  null,
    bio           varchar(256) null,
    gender        char(2)      null,
    birth         date         null,
    phone         varchar(20)  null,
    avatar_url    varchar(50)  null
);
