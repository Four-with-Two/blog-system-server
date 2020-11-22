package com.blog.system.dto;

import lombok.Data;

@Data
public class SendBlogDTO {
    private boolean code;
    private int author;
    private String avatar_url;
    private String name;
    private String bio;
    private String title;
    private String content;
    private String summary;
    private String publish_date;
    private String update_date;
}
