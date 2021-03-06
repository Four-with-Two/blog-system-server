package com.blog.system.model;

import lombok.Data;

@Data
public class Blog {
    private int id;
    private int author;
    private String title;
    private String content;
    private String summary;
    private String publish_date;
    private String update_date;
}
