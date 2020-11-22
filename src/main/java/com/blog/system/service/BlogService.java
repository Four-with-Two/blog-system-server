package com.blog.system.service;

import com.blog.system.dto.BlogsDTO;
import com.blog.system.dto.SendBlogDTO;
import com.blog.system.dto.SendBlogsDTO;
import com.blog.system.mapper.BlogMapper;
import com.blog.system.mapper.UserMapper;
import com.blog.system.model.Blog;
import com.blog.system.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlogService {

    @Autowired
    BlogMapper blogMapper;

    @Autowired
    UserMapper userMapper;
    //从数据库中获取一篇博客并组合其作者部分信息
    public SendBlogDTO get(int id) {
        Blog findBlog = blogMapper.findByID(id);
        if (findBlog == null) return null;
        User findUser = userMapper.findByID(findBlog.getAuthor());
        SendBlogDTO sendBlogDTO = new SendBlogDTO();
        sendBlogDTO.setCode(true);
        sendBlogDTO.setAuthor(findBlog.getAuthor());
        sendBlogDTO.setAvatar_url(findUser.getAvatar_url());
        if (findUser.getNick_name() == null || findUser.getNick_name().length() == 0)
            sendBlogDTO.setName(findUser.getUser_name());
        else sendBlogDTO.setName(findUser.getNick_name());
        sendBlogDTO.setBio(findUser.getBio());
        sendBlogDTO.setTitle(findBlog.getTitle());
        sendBlogDTO.setContent(findBlog.getContent());
        sendBlogDTO.setSummary(findBlog.getSummary());
        sendBlogDTO.setPublish_date(findBlog.getPublish_date());
        sendBlogDTO.setUpdate_date(findBlog.getUpdate_date());
        return sendBlogDTO;
    }
    //从数据库中获取多篇博客并组合其作者部分信息
    private List<BlogsDTO> combine(List<Blog> blogList, int id) {
        List<BlogsDTO> blogsDTOList = new ArrayList<BlogsDTO>();
        if(blogList!=null&&blogList.size()>0) {
            User user = null;
            if (id > 0) user = userMapper.findByID(id);
            for (Blog blog : blogList) {
                BlogsDTO blogsDTO = new BlogsDTO();
                blogsDTO.setId(blog.getId());
                if (id <= 0) user = userMapper.findByID(blog.getAuthor());
                blogsDTO.setAuthor(user.getId());
                blogsDTO.setAvatar_url(user.getAvatar_url());
                if (user.getNick_name() != null && user.getNick_name().length() == 0)
                    blogsDTO.setName(user.getNick_name());
                else blogsDTO.setName(user.getUser_name());
                blogsDTO.setTitle(blog.getTitle());
                blogsDTO.setSummary(blog.getSummary());
                blogsDTO.setPublish_date(blog.getPublish_date());
                blogsDTO.setUpdate_date(blog.getUpdate_date());
                blogsDTOList.add(blogsDTO);
            }
        }
        return blogsDTOList;
    }
    //获取多组博客信息 用户id=0时获取所有用户
    public SendBlogsDTO getBlogs(int id, int page) {
        SendBlogsDTO sendBlogsDTO = new SendBlogsDTO();
        sendBlogsDTO.setCode(true);
        List<Blog> blogList;
        if(id>0) blogList=blogMapper.findByAuthorID(id,(page-1)*10, 10);
        else blogList=blogMapper.findAll((page - 1) * 10, 10);
        sendBlogsDTO.setBlogsDTOList(combine(blogList, id));
        return sendBlogsDTO;
    }
}
