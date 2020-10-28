package com.blog.system.service;

import com.blog.system.dto.UserBlogDTO;
import com.blog.system.mapper.BlogMapper;
import com.blog.system.mapper.UserMapper;
import com.blog.system.model.Blog;
import com.blog.system.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserBlogService {

    @Autowired
    BlogMapper blogMapper;

    @Autowired
    UserMapper userMapper;

    public UserBlogDTO get(int id) {
        Blog blog=blogMapper.findByID(id);
        if(blog==null) return null;
        User user=userMapper.findByID(blog.getAuthor());
        if(user==null) return null;
        UserBlogDTO  userBlogDTO= new UserBlogDTO();
        userBlogDTO.setContent(blog.getEssay());
        userBlogDTO.setCreateTime(blog.getRelease_time());
        userBlogDTO.setDigest(blog.getBio());
        userBlogDTO.setTitle(blog.getTitle());
        String nick_name=user.getNick_name();
        if(nick_name!=null&&nick_name.length()>0)
            userBlogDTO.setUsername(nick_name);
        else
            userBlogDTO.setUsername(user.getUser_name());
        return userBlogDTO;
    }
}
