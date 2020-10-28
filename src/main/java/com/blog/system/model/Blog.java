package com.blog.system.model;

import lombok.Data;

@Data
public class Blog {
    private int id;
    private int author;
    private String title;
    private String essay;
    private String bio;
    private String release_time;
}
