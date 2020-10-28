package com.blog.system.model;

import lombok.Data;

@Data
public class User {
    private int id;
    private String user_name;
    private String nick_name;
    private String user_password;
    private String mail;
    private String avatar_url;
    private String bio;
    private String gender;
    private String birth;
    private String phone;
}
