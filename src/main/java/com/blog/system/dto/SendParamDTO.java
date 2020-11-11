package com.blog.system.dto;

import lombok.Data;

@Data
public class SendParamDTO<T> {
    private T object;
    private String code;
    private String message;
}
