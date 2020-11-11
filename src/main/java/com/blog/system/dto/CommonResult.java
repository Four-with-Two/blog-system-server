package com.blog.system.dto;

import lombok.Data;

@Data
public class CommonResult<T> {
    private T data;
    private String code;
    private String message;
}
