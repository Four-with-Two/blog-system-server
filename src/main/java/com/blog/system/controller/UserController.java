package com.blog.system.controller;

import com.alibaba.fastjson.JSON;
import com.blog.system.dto.SendStringDTO;
import com.blog.system.dto.SendUserDTO;
import com.blog.system.mapper.PersonalDataMapper;
import com.blog.system.mapper.UserMapper;
import com.blog.system.model.User;
import com.blog.system.util.JwtUtil;
import com.blog.system.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    JwtUtil jwtUtil;

    @Value("${domain}")
    String domain;

    //用户登录功能:验证成功返回用户id 验证失败返回错误信息
    @PostMapping("/login")
    public String login(@RequestBody User user) {
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
            return JSON.toJSONString(jwtUtil.createJWT(1000*60*60*24,findUser));
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
        User findMailUser = userMapper.findByMail(user.getMail());
        User findUserNameUser = userMapper.findByUser_name(user.getUser_name());
        SendStringDTO sendStringDTO = new SendStringDTO();
        if (findMailUser == null && findUserNameUser == null) {
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
            user.setAvatar_url(domain+"/avatar/"+"default.png");
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
    public String delete(@RequestHeader("token") String token) {
        if(token==null||jwtUtil.parseJWT(token)==null) {
            SendStringDTO sendStringDTO=new SendStringDTO();
            sendStringDTO.setCode(false);
            sendStringDTO.setMessage("User not logged in");
            return JSON.toJSONString(sendStringDTO);
        }
        String user_name=jwtUtil.parseJWT(token);
        userMapper.deleteByUser_name(user_name);
        personalDataMapper.delete(user_name);
        SendStringDTO sendStringDTO = new SendStringDTO();
        sendStringDTO.setCode(true);
        sendStringDTO.setMessage("OK");
        return JSON.toJSONString(sendStringDTO);
    }
}
