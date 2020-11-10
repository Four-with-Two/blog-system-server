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

    //写博客:将新博客写入数据库中，检查作者存不存在，不存在则返回错误信息
    @PostMapping("/newBlog")
    public String newBlog(@RequestHeader("token") String token, @RequestBody Blog blog) {
        if (token == null || !token.equals(acceptToken)) return null;
        if (blog.getAuthor() <= 0 || userMapper.findByID(blog.getAuthor()) == null) {
            SendStringDTO sendStringDTO = new SendStringDTO();
            sendStringDTO.setCode(false);
            sendStringDTO.setMessage("The author account does not exist");
            return JSON.toJSONString(sendStringDTO);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        blog.setPublish_date(simpleDateFormat.format(Calendar.getInstance().getTime()));
        blog.setUpdate_date(simpleDateFormat.format(Calendar.getInstance().getTime()));
        blogMapper.insert(blog);
        SendStringDTO sendStringDTO = new SendStringDTO();
        sendStringDTO.setCode(true);
        sendStringDTO.setMessage("OK");
        return JSON.toJSONString(sendStringDTO);
    }

    //获取某篇博客:获取数据库中对应博客id的博客并整合部分作者信息，若对应博客id的博客不存在则返回错误信息
    @GetMapping("/get/{id}")
    public String get(@RequestHeader("token") String token, @PathVariable("id") int id) {
        if (token == null || !token.equals(acceptToken)) return null;
        SendBlogDTO sendBlogDTO = blogService.get(id);
        if (sendBlogDTO == null) {
            SendStringDTO sendStringDTO = new SendStringDTO();
            sendStringDTO.setCode(false);
            sendStringDTO.setMessage("The blog does not exist");
            return JSON.toJSONString(sendStringDTO);
        }
        return JSON.toJSONString(sendBlogDTO);
    }

    //获取某个用户的博客:从数据库中获取对应用户id的博客，需要指定页数，返回List数据结构，按发布时间排序
    @GetMapping("/get/personal")
    public String getPersonal(@RequestHeader("token") String token, @RequestParam("id") int id, @RequestParam("page") int page) {
        if (token == null || !token.equals(acceptToken)) return null;
        if (userMapper.findByID(id) == null) {
            SendStringDTO sendStringDTO = new SendStringDTO();
            sendStringDTO.setCode(false);
            sendStringDTO.setMessage("The account does not exist");
        }
        return JSON.toJSONString(blogService.getBlogs(id, page));
    }

    //获取所有用户的博客:从数据库中获取所有博客，需要指定页数，返回List数据结构，按发布时间排序
    @GetMapping("/get/all")
    public String getAll(@RequestHeader("token") String token, @RequestParam("page") int page) {
        if (token == null || !token.equals(acceptToken)) return null;
        return JSON.toJSONString(blogService.getBlogs(0, page));
    }

    //计数某个用户的博客数量:从数据库中获取对应用户id的博客并计数，并返回博客总数和页数(每页10条信息)
    @GetMapping("/count/personal")
    public String getPersonal(@RequestHeader("token") String token, @RequestParam("id") int id) {
        if (token == null || !token.equals(acceptToken)) return null;
        SendCountDTO sendCountDTO = new SendCountDTO();
        sendCountDTO.setCode(true);
        sendCountDTO.setCnt(blogMapper.countByAuthorID(id));
        sendCountDTO.setPage((sendCountDTO.getCnt() + 9) / 10);
        return JSON.toJSONString(sendCountDTO);
    }

    //计数所有的博客数量:从数据库中获取所有博客并计数，并返回博客总数和页数(每页10条信息)
    @GetMapping("/count/all")
    public String countAll(@RequestHeader("token") String token) {
        if (token == null || !token.equals(acceptToken)) return null;
        SendCountDTO sendCountDTO = new SendCountDTO();
        sendCountDTO.setCode(true);
        sendCountDTO.setCnt(blogMapper.countAll());
        sendCountDTO.setPage((sendCountDTO.getCnt() + 9) / 10);
        return JSON.toJSONString(sendCountDTO);
    }

    //删除博客:删除数据库中对应博客id的博客信息
    @GetMapping("/delete")
    public String delete(@RequestHeader("token") String token, @RequestParam("id") int id) {
        if (token == null || !token.equals(acceptToken)) return null;
        SendStringDTO sendStringDTO = new SendStringDTO();
        blogMapper.delete(id);
        sendStringDTO.setCode(true);
        sendStringDTO.setMessage("OK");
        return JSON.toJSONString(sendStringDTO);
    }

    //修改博客:修改数据库中对应博客id的博客信息，若博客不存在则返回错误信息
    @PostMapping("/modify/{id}")
    public String modify(@RequestHeader("token") String token, @PathVariable("id") int id, @RequestBody Blog blog) {
        if (token == null || !token.equals(acceptToken)) return null;
        SendStringDTO sendStringDTO = new SendStringDTO();
        if (blogMapper.findByID(id) != null) {
            blog.setId(id);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            blog.setUpdate_date(simpleDateFormat.format(Calendar.getInstance().getTime()));
            blogMapper.update(blog);
            sendStringDTO.setCode(true);
            sendStringDTO.setMessage("OK");
        } else {
            sendStringDTO.setCode(false);
            sendStringDTO.setMessage("The blog does not exist");
        }
        return JSON.toJSONString(sendStringDTO);
    }
}
