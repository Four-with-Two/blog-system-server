package com.blog.system.controller;

import com.alibaba.fastjson.JSON;
import com.blog.system.dto.SendDTO;
import com.blog.system.dto.UserBlogDTO;
import com.blog.system.mapper.BlogMapper;
import com.blog.system.model.Blog;
import com.blog.system.service.UserBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    BlogMapper blogMapper;

    @Autowired
    UserBlogService userBlogService;

    @PostMapping("/newBlog")
    public String newBlog(@RequestBody Blog blog) {
        SendDTO object = new SendDTO();
        blogMapper.insert(blog);
        object.setCode(true);
        object.setObject("OK");
        return JSON.toJSONString(object);
    }

    @GetMapping("/exhibition")
    public String exhibition(@RequestParam("id") int id) {
        SendDTO object = new SendDTO();
        UserBlogDTO userBlogDTO=userBlogService.get(id);
        if(userBlogDTO==null) {
            object.setCode(false);
            object.setObject("FAIL");
        }
        else {
            object.setCode(true);
            object.setObject(userBlogDTO);
        }
        return JSON.toJSONString(object);
    }

    @GetMapping("/elimination")
    public String elimination(@RequestParam("id") int id) {
        SendDTO object=new SendDTO();
        blogMapper.delete(id);
        object.setCode(true);
        object.setObject("OK");
        return JSON.toJSONString(object);
    }

    @PostMapping("/alteration/{id}")
    public String alteration(@PathVariable("id") int id, @RequestBody Blog blog) {
        blog.setId(id);
        SendDTO object=new SendDTO();
        if(blogMapper.findByID(blog.getId())==null) {
            object.setCode(false);
            object.setObject("Blog does not exist");
        }
        else {
            object.setCode(true);
            blogMapper.update(blog);
            object.setObject("OK");
        }
        return JSON.toJSONString(object);
    }
}
