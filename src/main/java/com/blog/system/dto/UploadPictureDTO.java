package com.blog.system.dto;

import lombok.Data;

import java.net.URL;

@Data
public class UploadPictureDTO {
    private int width;
    private int height;
    private URL url;
}
