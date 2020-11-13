package com.blog.system.dto;

import lombok.Data;

@Data
public class SendUserDTO {
    private boolean code;
    private String name;
    private String avatar_url;
}
