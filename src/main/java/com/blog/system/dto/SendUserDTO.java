package com.blog.system.dto;

import lombok.Data;

@Data
public class SendUserDTO {
    private boolean code;
    private int id;
    private String user_name;
    private String mail;
    private String nick_name;
    private String birthday;
    private String gender;
    private String bio;
    private String avatar_url;
    private String phone;
}
