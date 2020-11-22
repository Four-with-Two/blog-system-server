package com.blog.system.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.blog.system.dto.*;
import com.blog.system.mapper.UserMapper;
import com.blog.system.model.User;
import com.blog.system.util.JwtUtil;
import com.blog.system.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/picture")
public class PictureController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private RedisUtil redisUtil;

    @Value("${domain}")
    String domain;

    @Value("${binDir}")
    String binDir;

    //上传个人头像
    @PostMapping(value = "/avatar/upload",produces = "text/plain;charset=UTF-8")
    String uploadPicture(@RequestHeader("token") String token,
                                                 @RequestParam("image")MultipartFile avatar){
        if(token==null||jwtUtil.parseJWT(token)==null) {
            SendStringDTO sendStringDTO=new SendStringDTO();
            sendStringDTO.setCode(false);
            sendStringDTO.setMessage("User not logged in");
            return JSON.toJSONString(sendStringDTO);
        }
        if(avatar.isEmpty()) {
            SendStringDTO sendStringDTO=new SendStringDTO();
            sendStringDTO.setCode(false);
            sendStringDTO.setMessage("avatar is null");
            return JSON.toJSONString(sendStringDTO);
        }
        String fileName = avatar.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        if(!suffixName.equals(".jpg") && !suffixName.equals(".jpeg") && !suffixName.equals(".png") ) {
            SendStringDTO sendStringDTO=new SendStringDTO();
            sendStringDTO.setCode(false);
            sendStringDTO.setMessage("avatar must be .jpg .jpeg .png");
            return JSON.toJSONString(sendStringDTO);
        }
        try {
            BufferedImage bufferedImage= ImageIO.read(avatar.getInputStream());
            int height=bufferedImage.getHeight();
            int width=bufferedImage.getWidth();
            if(height==0||width==0) {
                SendStringDTO sendStringDTO=new SendStringDTO();
                sendStringDTO.setCode(false);
                sendStringDTO.setMessage("avatar illegal");
                return JSON.toJSONString(sendStringDTO);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            SendStringDTO sendStringDTO=new SendStringDTO();
            sendStringDTO.setCode(false);
            sendStringDTO.setMessage("avatar check fail");
            return JSON.toJSONString(sendStringDTO);
        }
        User user=userMapper.findByUser_name(jwtUtil.parseJWT(token));
        if(user.getAvatar_url().equals(domain+"/avatar/"+"default.png")) {
            fileName=UUID.randomUUID().toString()+suffixName;
        }
        else fileName=user.getAvatar_url().substring(user.getAvatar_url().lastIndexOf("/")+1);
        File file = new File(binDir+"/image/"+fileName);
        if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        try {
            avatar.transferTo(file);
        }
        catch (Exception e) {
            e.printStackTrace();
            SendStringDTO sendStringDTO=new SendStringDTO();
            sendStringDTO.setCode(false);
            sendStringDTO.setMessage("avatar upload fail");
            return JSON.toJSONString(sendStringDTO);
        }
        String url=domain+"/avatar/"+fileName;
        userMapper.updatePicture(url,user.getUser_name());
        SendStringDTO sendStringDTO=new SendStringDTO();
        sendStringDTO.setCode(true);
        sendStringDTO.setMessage("ok");
        return JSON.toJSONString(sendStringDTO);
    }

    //获取用户头像url
    @GetMapping("/avatar/get")
    String get(@RequestHeader("token")String token,@RequestParam("user_name")String user_name) {
        User user=userMapper.findByUser_name(user_name);
        if(user==null) {
            SendStringDTO sendStringDTO = new SendStringDTO();
            sendStringDTO.setCode(false);
            sendStringDTO.setMessage("The account does not exist");
            return JSON.toJSONString(sendStringDTO);
        }
        return JSON.toJSONString(user.getAvatar_url());
    }

    // 获取图形验证码

    @GetMapping("/captcha")
    CommonResult<UrlUUIDDTO> getCaptcha() {
        CommonResult commonResult=new CommonResult();
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(112, 38);
        try {
            String verification_code=lineCaptcha.getCode();
            String simpleUUID = IdUtil.simpleUUID();
            //文件实现上传
            String dateDir=new SimpleDateFormat("yyyy/MM/dd/")
                    .format(new Date());
            String fileDirPath=binDir+"/image/"+dateDir;
            File dirFile=new File(fileDirPath);
            if(!dirFile.exists()){
                dirFile.mkdirs();
            }
            String realFileName=simpleUUID;
            File avatarFile=new File(fileDirPath+realFileName);
            lineCaptcha.write(avatarFile);
            redisUtil.set(simpleUUID,verification_code);
            UrlUUIDDTO urlUUIDDTO=new UrlUUIDDTO();
            urlUUIDDTO.setUrl(avatarFile.toURI().toURL());
            urlUUIDDTO.setUuid(simpleUUID);
//            System.out.println(verification_code);
//            System.out.println(simpleUUID);
//            System.out.println(urlUUIDDTO.getUrl());
            commonResult.setData(urlUUIDDTO);
            commonResult.setCode("6666");
            commonResult.setMessage("操作成功！");
            return commonResult;
        } catch (MalformedURLException e) {
            commonResult.setCode("5555");
            commonResult.setMessage("图形验证码创建失败");
            return commonResult;
        }
    }

    /**
     * 校验输入的验证码
     * @param simpleUUID
     * @param
     * @return
     */
    @PostMapping("/verification")
    CommonResult verification(@RequestHeader("simpleUUID")String simpleUUID,
                              @RequestBody VeriCodeDTO veriCodeDTO) {
        CommonResult commonResult=new CommonResult();
        String temp= (String) redisUtil.get(simpleUUID);
        if(temp.equals(veriCodeDTO.getVerification_code())){
            commonResult.setCode("6666");
            commonResult.setMessage("操作成功！");
            return commonResult;
        }
        else{
            commonResult.setCode("2003");
            commonResult.setMessage("验证失败！");
            return commonResult;
        }
    }

}
