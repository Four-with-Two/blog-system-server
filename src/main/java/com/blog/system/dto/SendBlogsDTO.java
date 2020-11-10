package com.blog.system.dto;

import lombok.Data;

import java.util.List;

@Data
public class SendBlogsDTO {
    private boolean code;
    private List<BlogsDTO> blogsDTOList;
}
