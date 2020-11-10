package com.blog.system.dto;

import lombok.Data;

@Data
public class BlogsDTO {
    private int id;
    private int author;
    private String avatar_url;
    private String name;
    private String title;
    private String summary;
    private String publish_date;
    private String update_date;
}
