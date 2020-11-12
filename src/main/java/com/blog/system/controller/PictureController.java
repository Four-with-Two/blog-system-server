package com.blog.system.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.IdUtil;
import com.blog.system.dto.CommonResult;
import com.blog.system.mapper.UserMapper;
import com.blog.system.util.JwtUtil;
import com.blog.system.util.RedisUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

@RestController
@RequestMapping("/picture")
public class PictureController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private RedisUtil redisUtil;

    /**
     * 上传个人头像
     * @param token
     * @param avatar
     * @return
     */
    @PostMapping("/avatar/upload")
    CommonResult<String> uploadPicture(@RequestHeader("token") String token,
                                       @RequestBody File avatar){
        CommonResult commonResult=new CommonResult();
        if(true==jwtUtil.isVerify(token)){
            Claims claims=jwtUtil.parseJWT(token);
            String user_name=claims.get("user_name",String.class);
            String avatar_url=(avatar.toURI()).getPath();
            userMapper.updatePicture(avatar_url,user_name);
            commonResult.setData(avatar_url);
            commonResult.setCode("6666");
            commonResult.setMessage("操作成功！");
            return commonResult;
        }
        else{
            commonResult.setCode("1006");
            commonResult.setMessage("用户无有效令牌!");
            return commonResult;
        }
    }

    /**
     * 修改个人头像
     * @param token
     * @param avatar_url
     * @return
     */
    @PutMapping("/avatar/alteration")
    CommonResult changePicture(@RequestHeader("token") String token,
                               @RequestBody String avatar_url){
        CommonResult commonResult=new CommonResult();
        if(true==jwtUtil.isVerify(token)){
            Claims claims=jwtUtil.parseJWT(token);
            String user_name=claims.get("user_name",String.class);
            userMapper.updatePicture(avatar_url,user_name);
            commonResult.setCode("6666");
            commonResult.setMessage("操作成功！");
            return commonResult;
        }
        else{
            commonResult.setCode("1006");
            commonResult.setMessage("用户无有效令牌!");
            return commonResult;
        }
    }

    /**
     *获取用户头像
     * @param token
     * @return
     */
    @GetMapping("/avatar")
    CommonResult<String> getPicture(@RequestHeader("token") String token){
        CommonResult commonResult=new CommonResult();
        if(true==jwtUtil.isVerify(token)){
            Claims claims=jwtUtil.parseJWT(token);
            String user_name=claims.get("user_name",String.class);
            String avatar_url=userMapper.getAvatar(user_name);
            commonResult.setData(avatar_url);
            commonResult.setCode("6666");
            commonResult.setMessage("操作成功！");
            return commonResult;
        }
        else{
            commonResult.setCode("1006");
            commonResult.setMessage("用户无有效令牌!");
            return commonResult;
        }
    }

    /**
     * 获取图形验证码
     * @return
     */
    @GetMapping("/captcha")
    CommonResult<Map<String,URL>> getCaptcha() {
        CommonResult commonResult=new CommonResult();
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
        try {
            Map<String,URL> temp = null;
            URL captcha= new URL(lineCaptcha.toString());
            String verification_code=lineCaptcha.getCode();
            String simpleUUID = IdUtil.simpleUUID();
            redisUtil.set(simpleUUID,verification_code);
            temp.put(simpleUUID,captcha);
            commonResult.setData(temp);
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
     * @param verification_code
     * @return
     */
    @PostMapping("/verification")
    CommonResult verification(@RequestHeader("simpleUUID")String simpleUUID,
                              @RequestBody String verification_code) {
        CommonResult commonResult=new CommonResult();
        String temp= (String) redisUtil.get(simpleUUID);
        if(temp==verification_code){
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
