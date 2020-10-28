package com.blog.system.dto;

import lombok.Data;

@Data
public class UserBlogDTO {
    private String username;
    private String title;
    private String digest;
    private String content;
    private String createTime;
}
