create table personal_data
(
    id         int auto_increment
        primary key,
    user_name  varchar(20)  not null,
    mail       char(30)     not null,
    nick_name  varchar(20)  null,
    birthday   char(20)     null,
    gender     char(2)      null,
    profile    varchar (256) null,
    phone      char(20)     null,
    constraint user_mail_uindex
        unique (mail),
    constraint user_user_name_uindex
        unique (user_name)
);