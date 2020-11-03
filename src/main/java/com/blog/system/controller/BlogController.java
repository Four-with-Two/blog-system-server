package com.blog.system.controller;

import com.alibaba.fastjson.JSON;
import com.blog.system.dto.SendBlogDTO;
import com.blog.system.dto.SendStringDTO;
import com.blog.system.mapper.BlogMapper;
import com.blog.system.model.Blog;
import com.blog.system.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blog")
public class BlogController {

    @Value("${acceptToken}")
    String acceptToken;

    @Autowired
    BlogMapper blogMapper;

    @PostMapping("/new")
    public String newBlog(@RequestHeader("token") String token, @RequestBody Blog blog) {
        if (token == null || !token.equals(acceptToken)) return null;
        //整型未赋值时处理(初值问题)
        if (blog.getAuthor() == 0) {
            SendStringDTO sendStringDTO = new SendStringDTO();
            sendStringDTO.setCode(false);
            sendStringDTO.setString("format error");
            return JSON.toJSONString(sendStringDTO);
        }
        blogMapper.insert(blog);
        SendStringDTO sendStringDTO = new SendStringDTO();
        sendStringDTO.setCode(true);
        sendStringDTO.setString("OK");
        return JSON.toJSONString(sendStringDTO);
    }

    @GetMapping("/get/{id}")
    public String exhibition(@RequestHeader("token") String token,@PathVariable("id") int id) {
        if (token == null || !token.equals(acceptToken)) return null;
        Blog findBlog=blogMapper.findByID(id);
        if(findBlog==null) {
            SendStringDTO sendStringDTO=new SendStringDTO();
            sendStringDTO.setCode(false);
            sendStringDTO.setString("The blog does not exist");
            return JSON.toJSONString(sendStringDTO);
        }
        SendBlogDTO sendBlogDTO=new SendBlogDTO();
        sendBlogDTO.setCode(true);
        sendBlogDTO.setId(findBlog.getId());
        sendBlogDTO.setAuthor(findBlog.getAuthor());
        sendBlogDTO.setTitle(findBlog.getTitle());
        sendBlogDTO.setContent(findBlog.getContent());
        sendBlogDTO.setSummary(findBlog.getSummary());
        sendBlogDTO.setPublish_date(findBlog.getPublish_date());
        sendBlogDTO.setUpdate_date(findBlog.getUpdate_date());
        return JSON.toJSONString(sendBlogDTO);
    }

    @GetMapping("/delete")
    public String delete(@RequestHeader("token") String token,@RequestParam("id") int id) {
        if (token == null || !token.equals(acceptToken)) return null;
        Blog findBlog=blogMapper.findByID(id);
        SendStringDTO sendStringDTO=new SendStringDTO();
        if(findBlog==null) {
            sendStringDTO.setCode(false);
            sendStringDTO.setString("The blog does not exist");
        }
        else {
            blogMapper.delete(id);
            sendStringDTO.setCode(true);
            sendStringDTO.setString("OK");
        }
        return JSON.toJSONString(sendStringDTO);
    }

    @PostMapping("/modify/{id}")
    public String alteration(@RequestHeader("token") String token,@PathVariable("id") int id, @RequestBody Blog blog) {
        if (token == null || !token.equals(acceptToken)) return null;
        Blog findBlog=blogMapper.findByID(id);
        SendStringDTO sendStringDTO=new SendStringDTO();
        if(findBlog!=null) {
            blogMapper.update(blog);
            sendStringDTO.setCode(true);
            sendStringDTO.setString("OK");
        }
        else {
            sendStringDTO.setCode(false);
            sendStringDTO.setString("The blog does not exist");
        }
        return JSON.toJSONString(sendStringDTO);
    }
}
