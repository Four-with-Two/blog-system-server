package com.blog.system.controller;

import com.alibaba.fastjson.JSON;
import com.blog.system.dto.SendStringDTO;
import com.blog.system.dto.SendUserDTO;
import com.blog.system.mapper.PersonalDataMapper;
import com.blog.system.mapper.UserMapper;
import com.blog.system.model.User;
import com.blog.system.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired(required = false)
    private PersonalDataMapper personalDataMapper;

    @Autowired
    RedisUtil redisUtil;

    //用户登录功能:验证成功返回用户id 验证失败返回错误信息
    @PostMapping("/login")
    public String login(@RequestBody User user, HttpServletRequest request) {
        if (user.getMail() == null && user.getUser_name() == null || user.getPassword() == null) {
            SendStringDTO sendStringDTO=new SendStringDTO();
            sendStringDTO.setCode(false);
            sendStringDTO.setMessage("format error");
            return JSON.toJSONString(sendStringDTO);
        }
        User findUser;
        if (user.getMail() != null) findUser = userMapper.findByMail(user.getMail());
        else findUser = userMapper.findByUser_name(user.getUser_name());
        if (findUser == null) {
            SendStringDTO sendStringDTO = new SendStringDTO();
            sendStringDTO.setCode(false);
            sendStringDTO.setMessage("The account does not exist");
            return JSON.toJSONString(sendStringDTO);
        } else if (findUser.getPassword().equals(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()))) {
            request.getSession().setAttribute("id", user.getId());
            SendUserDTO sendUserDTO = new SendUserDTO();
            sendUserDTO.setCode(true);
            if(user.getNick_name()==null||user.getNick_name().length()==0)
                sendUserDTO.setName(user.getUser_name());
            else sendUserDTO.setName(user.getNick_name());
            sendUserDTO.setAvatar_url(user.getAvatar_url());
            return JSON.toJSONString(sendUserDTO);
        } else {
            SendStringDTO sendStringDTO = new SendStringDTO();
            sendStringDTO.setCode(false);
            sendStringDTO.setMessage("Incorrect password");
            return JSON.toJSONString(sendStringDTO);
        }

    }
    //用户注册功能:若注册成功则将用户信息添加进数据库 返回注册状态
    @PostMapping("/register")
    public String register(@RequestBody User user) {
        User findUser = userMapper.findByMail(user.getMail());
        SendStringDTO sendStringDTO = new SendStringDTO();
        if (findUser == null) {
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
            userMapper.insertUser(user);
            personalDataMapper.insertUser(user);
            sendStringDTO.setCode(true);
            sendStringDTO.setMessage("OK");
        } else {
            sendStringDTO.setCode(false);
            sendStringDTO.setMessage("The account already exists");
        }
        return JSON.toJSONString(sendStringDTO);
    }

    //删除用户:删除数据库的用户信息
    @GetMapping("/delete")
    public String delete(HttpServletRequest request) {
        if(request.getSession().getAttribute("id")==null) {
            SendStringDTO sendStringDTO=new SendStringDTO();
            sendStringDTO.setCode(false);
            sendStringDTO.setMessage("User not logged in");
            return JSON.toJSONString(sendStringDTO);
        }
        int id=(int)request.getSession().getAttribute("id");
        User user=userMapper.findByID(id);
        userMapper.delete(id);
        personalDataMapper.delete(user.getUser_name());
        request.getSession().removeAttribute("id");
        SendStringDTO sendStringDTO = new SendStringDTO();
        sendStringDTO.setCode(true);
        sendStringDTO.setMessage("OK");
        return JSON.toJSONString(sendStringDTO);
    }

    //登出
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        if(request.getSession().getAttribute("id")==null) {
            SendStringDTO sendStringDTO=new SendStringDTO();
            sendStringDTO.setCode(false);
            sendStringDTO.setMessage("User not logged in");
            return JSON.toJSONString(sendStringDTO);
        }
        int id=(int)request.getSession().getAttribute("id");
        request.getSession().removeAttribute("id");
        SendStringDTO sendStringDTO = new SendStringDTO();
        sendStringDTO.setCode(true);
        sendStringDTO.setMessage("OK");
        return JSON.toJSONString(sendStringDTO);
    }
}
