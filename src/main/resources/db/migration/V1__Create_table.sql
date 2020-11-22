create table blog
(
    id           int auto_increment
        primary key,
    author       int         not null,
    title        varchar(50) null,
    content      text        null,
    summary      varchar(50) null,
    publish_date char(20)    null,
    update_date  char(20)    null
);

create table user
(
    id         int auto_increment
        primary key,
    user_name  varchar(20)  not null,
    mail       char(30)     not null,
    password   varchar(255) not null,
    nick_name  varchar(20)  null,
    birthday   char(20)     null,
    gender     char(2)      null,
    bio        varchar(200) null,
    avatar_url varchar(256)  null,
    phone      char(20)     null,
    constraint user_mail_uindex
        unique (mail),
    constraint user_user_name_uindex
        unique (user_name)
);