package com.blog.system.model;

import lombok.Data;

@Data
public class User {
    private int id;
    private String user_name;
    private String mail;
    private String password;
    private String nick_name;
    private String birthday;
    private String gender;
    private String bio;
    private String avatar_url;
    private String phone;
}
