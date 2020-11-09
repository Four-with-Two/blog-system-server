package com.blog.system.controller;

import com.alibaba.fastjson.JSON;
import com.blog.system.dto.SendStringDTO;
import com.blog.system.dto.SendUserDTO;
import com.blog.system.mapper.UserMapper;
import com.blog.system.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Value("${acceptToken}")
    private String acceptToken;

    //用户登录功能:验证成功返回用户id 验证失败返回错误信息
    @PostMapping("/login")
    public String login(@RequestHeader("token") String token, @RequestBody User user) {
        if (token == null || !token.equals(acceptToken)) return null;
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
            SendUserDTO sendUserDTO = new SendUserDTO();
            sendUserDTO.setCode(true);
            sendUserDTO.setId(findUser.getId());
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
    public String register(@RequestHeader("token") String token, @RequestBody User user) {
        if (token == null || !token.equals(acceptToken)) return null;
        User findUser = userMapper.findByMail(user.getMail());
        SendStringDTO sendStringDTO = new SendStringDTO();
        if (findUser == null) {
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
            userMapper.insertUser(user);
            sendStringDTO.setCode(true);
            sendStringDTO.setMessage("OK");
        } else {
            sendStringDTO.setCode(false);
            sendStringDTO.setMessage("The account already exists");
        }
        return JSON.toJSONString(sendStringDTO);
    }

    //删除用户:删除数据库中对应id的用户信息
    @GetMapping("/delete")
    public String delete(@RequestHeader("token") String token, @RequestParam("id") int id) {
        if (token == null || !token.equals(acceptToken)) return null;
        SendStringDTO sendStringDTO = new SendStringDTO();
        userMapper.delete(id);
        sendStringDTO.setCode(true);
        sendStringDTO.setMessage("OK");
        return JSON.toJSONString(sendStringDTO);
    }

    //获取某用户信息:获取数据库中对应id的用户信息并返回，若不存在则返回错误信息
    @GetMapping("/get/{id}")
    public String get(@RequestHeader("token") String token,@PathVariable("id") int id) {
        if (token == null || !token.equals(acceptToken)) return null;
        User findUser=userMapper.findByID(id);
        if(findUser!=null) {
            SendUserDTO sendUserDTO = new SendUserDTO();
            sendUserDTO.setCode(true);
            sendUserDTO.setUser_name(findUser.getUser_name());
            sendUserDTO.setMail(findUser.getMail());
            sendUserDTO.setNick_name(findUser.getNick_name());
            sendUserDTO.setGender(findUser.getGender());
            sendUserDTO.setBirthday(findUser.getBirthday());
            sendUserDTO.setBio(findUser.getBio());
            sendUserDTO.setAvatar_url(findUser.getAvatar_url());
            sendUserDTO.setPhone(findUser.getPhone());
            return JSON.toJSONString(sendUserDTO);
        }
        else {
            SendStringDTO sendStringDTO=new SendStringDTO();
            sendStringDTO.setCode(false);
            sendStringDTO.setMessage("The account does not exist");
            return JSON.toJSONString(sendStringDTO);
        }
    }
}
