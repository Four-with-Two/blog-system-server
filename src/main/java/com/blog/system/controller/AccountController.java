package com.blog.system.controller;

import com.alibaba.fastjson.JSON;
import com.blog.system.dto.SendDTO;
import com.blog.system.mapper.UserMapper;
import com.blog.system.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class AccountController {

    @Autowired
    UserMapper userMapper;

    @GetMapping("/login")
    public String login(@RequestParam("identification") String identification, @RequestParam("password") String password) {
        User user = userMapper.findByMail(identification);
        SendDTO object = new SendDTO();
        if (user == null) {
            object.setCode(false);
            object.setObject("Account does not exist");
        }
        else if (user.getUser_password().equals(password)) {
            object.setCode(true);
            object.setObject("OK");
        }
        else {
            object.setCode(false);
            object.setObject("Incorrect password");
        }
        return JSON.toJSONString(object);
    }

    @GetMapping("/registration")
    public String registration(@RequestParam("identification") String
                                       identification, @RequestParam("password") String password) {
        User user = userMapper.findByMail(identification);
        SendDTO object = new SendDTO();
        if(user==null) {
            userMapper.insertUser(identification,password);
            object.setCode(true);
            object.setObject("OK");
        }
        else {
            object.setCode(false);
            object.setObject("Account already exists");
        }
        return JSON.toJSONString(object);
    }
}
