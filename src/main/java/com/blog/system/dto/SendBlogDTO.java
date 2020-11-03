package com.blog.system.dto;

import lombok.Data;

@Data
public class SendBlogDTO {
    private boolean code;
    private int id;
    private int author;
    private String title;
    private String content;
    private String summary;
    private String publish_date;
    private String update_date;
}
