package com.blog.system.controller;

import com.alibaba.fastjson.JSON;
import com.blog.system.dto.SendBlogDTO;
import com.blog.system.dto.SendBlogsDTO;
import com.blog.system.dto.SendCountDTO;
import com.blog.system.dto.SendStringDTO;
import com.blog.system.mapper.BlogMapper;
import com.blog.system.mapper.UserMapper;
import com.blog.system.model.Blog;
import com.blog.system.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@RestController
@RequestMapping("/blog")
public class BlogController {

    @Value("${acceptToken}")
    String acceptToken;

    @Autowired
    BlogMapper blogMapper;

    @Autowired
    BlogService blogService;

    @Autowired
    UserMapper userMapper;

    @PostMapping("/newBlog")
    public String newBlog(@RequestHeader("token") String token, @RequestBody Blog blog) {
        if (token == null || !token.equals(acceptToken)) return null;
        if (blog.getAuthor() <= 0 || userMapper.findByID(blog.getAuthor()) == null) {
            SendStringDTO sendStringDTO = new SendStringDTO();
            sendStringDTO.setCode(false);
            sendStringDTO.setStr("The author account does not exist");
            return JSON.toJSONString(sendStringDTO);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        blog.setPublish_date(simpleDateFormat.format(Calendar.getInstance().getTime()));
        blog.setUpdate_date(simpleDateFormat.format(Calendar.getInstance().getTime()));
        blogMapper.insert(blog);
        SendStringDTO sendStringDTO = new SendStringDTO();
        sendStringDTO.setCode(true);
        sendStringDTO.setStr("OK");
        return JSON.toJSONString(sendStringDTO);
    }

    @GetMapping("/get/{id}")
    public String get(@RequestHeader("token") String token, @PathVariable("id") int id) {
        if (token == null || !token.equals(acceptToken)) return null;
        SendBlogDTO sendBlogDTO = blogService.get(id);
        if (sendBlogDTO == null) {
            SendStringDTO sendStringDTO = new SendStringDTO();
            sendStringDTO.setCode(false);
            sendStringDTO.setStr("The blog does not exist");
            return JSON.toJSONString(sendStringDTO);
        }
        return JSON.toJSONString(sendBlogDTO);
    }

    @GetMapping("/get/personal")
    public String getPersonal(@RequestHeader("token") String token, @RequestParam("id") int id, @RequestParam("page") int page) {
        if (token == null || !token.equals(acceptToken)) return null;
        SendBlogsDTO sendBlogsDTO = blogService.getPersonal(id, page);
        if (!sendBlogsDTO.isCode()) {
            SendStringDTO sendStringDTO = new SendStringDTO();
            sendStringDTO.setCode(false);
            sendStringDTO.setStr("The account does not exist");
        }
        return JSON.toJSONString(sendBlogsDTO);
    }

    @GetMapping("/get/all")
    public String getAll(@RequestHeader("token") String token, @RequestParam("page") int page) {
        if (token == null || !token.equals(acceptToken)) return null;
        SendBlogsDTO sendBlogsDTO = blogService.getAll(page);
        return JSON.toJSONString(sendBlogsDTO);
    }

    @GetMapping("/count/personal")
    public String getPersonal(@RequestHeader("token") String token, @RequestParam("id") int id) {
        if (token == null || !token.equals(acceptToken)) return null;
        SendCountDTO sendCountDTO = new SendCountDTO();
        sendCountDTO.setCode(true);
        sendCountDTO.setCnt(blogMapper.countByAuthorID(id));
        sendCountDTO.setPage((sendCountDTO.getCnt() + 9) / 10);
        return JSON.toJSONString(sendCountDTO);
    }

    @GetMapping("/count/all")
    public String countAll(@RequestHeader("token") String token) {
        if (token == null || !token.equals(acceptToken)) return null;
        SendCountDTO sendCountDTO = new SendCountDTO();
        sendCountDTO.setCode(true);
        sendCountDTO.setCnt(blogMapper.countAll());
        sendCountDTO.setPage((sendCountDTO.getCnt() + 9) / 10);
        return JSON.toJSONString(sendCountDTO);
    }

    @GetMapping("/delete")
    public String delete(@RequestHeader("token") String token, @RequestParam("id") int id) {
        if (token == null || !token.equals(acceptToken)) return null;
        SendStringDTO sendStringDTO = new SendStringDTO();
        blogMapper.delete(id);
        sendStringDTO.setCode(true);
        sendStringDTO.setStr("OK");
        return JSON.toJSONString(sendStringDTO);
    }

    @PostMapping("/modify/{id}")
    public String modify(@RequestHeader("token") String token, @PathVariable("id") int id, @RequestBody Blog blog) {
        if (token == null || !token.equals(acceptToken)) return null;
        Blog findBlog = blogMapper.findByID(id);
        SendStringDTO sendStringDTO = new SendStringDTO();
        if (findBlog != null) {
            blog.setPublish_date(findBlog.getPublish_date());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            blog.setUpdate_date(simpleDateFormat.format(Calendar.getInstance().getTime()));
            blogMapper.update(blog);
            sendStringDTO.setCode(true);
            sendStringDTO.setStr("OK");
        } else {
            sendStringDTO.setCode(false);
            sendStringDTO.setStr("The blog does not exist");
        }
        return JSON.toJSONString(sendStringDTO);
    }
}
