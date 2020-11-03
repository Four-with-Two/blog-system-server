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

    @PostMapping("/login")
    public String login(@RequestHeader("token") String token, @RequestBody User user) {
        if (token == null || !token.equals(acceptToken)) return null;
        if (user.getMail() == null && user.getUser_name() == null || user.getPassword() == null) {
            SendStringDTO sendStringDTO=new SendStringDTO();
            sendStringDTO.setCode(false);
            sendStringDTO.setString("format error");
            return JSON.toJSONString(sendStringDTO);
        }
        User findUser;
        if (user.getMail() != null) findUser = userMapper.findByMail(user.getMail());
        else findUser = userMapper.findByUser_name(user.getUser_name());
        if (findUser == null) {
            SendStringDTO sendStringDTO = new SendStringDTO();
            sendStringDTO.setCode(false);
            sendStringDTO.setString("The account does not exist");
            return JSON.toJSONString(sendStringDTO);
        } else if (findUser.getPassword().equals(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()))) {
            SendUserDTO sendUserDTO = new SendUserDTO();
            sendUserDTO.setCode(true);
            sendUserDTO.setId(findUser.getId());
            return JSON.toJSONString(sendUserDTO);
        } else {
            SendStringDTO sendStringDTO = new SendStringDTO();
            sendStringDTO.setCode(false);
            sendStringDTO.setString("Incorrect password");
            return JSON.toJSONString(sendStringDTO);
        }

    }

    @PostMapping("/register")
    public String registration(@RequestHeader("token") String token, @RequestBody User user) {
        if (token == null || !token.equals(acceptToken)) return null;
        User findUser = userMapper.findByMail(user.getMail());
        SendStringDTO sendStringDTO = new SendStringDTO();
        if (findUser == null) {
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
            userMapper.insertUser(user);
            sendStringDTO.setCode(true);
            sendStringDTO.setString("OK");
        } else {
            sendStringDTO.setCode(false);
            sendStringDTO.setString("The account already exists");
        }
        return JSON.toJSONString(sendStringDTO);
    }

    @GetMapping("/delete")
    public String delete(@RequestHeader("token") String token, @RequestParam("id") int id) {
        if (token == null || !token.equals(acceptToken)) return null;
        User findUser = userMapper.findByID(id);
        SendStringDTO sendStringDTO = new SendStringDTO();
        if (findUser == null) {
            sendStringDTO.setCode(false);
            sendStringDTO.setString("The account does not exist");
        } else {
            userMapper.delete(id);
            sendStringDTO.setCode(true);
            sendStringDTO.setString("OK");
        }
        return JSON.toJSONString(sendStringDTO);
    }

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
            sendStringDTO.setString("The account does not exist");
            return JSON.toJSONString(sendStringDTO);
        }
    }
}
