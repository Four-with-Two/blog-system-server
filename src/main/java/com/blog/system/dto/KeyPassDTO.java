package com.blog.system.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class KeyPassDTO {
    private String session_key;
    private String password;
}
