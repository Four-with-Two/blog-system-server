package com.blog.system.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.IdUtil;
import com.blog.system.dto.CommonResult;
import com.blog.system.dto.UploadPictureDTO;
import com.blog.system.dto.UrlUUIDDTO;
import com.blog.system.dto.VeriCodeDTO;
import com.blog.system.mapper.UserMapper;
import com.blog.system.util.JwtUtil;
import com.blog.system.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
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

    @Value("classpath:static")
    private String path;

    /**
     * 上传个人头像
     * @param token
     * @param avatar
     * @return
     */
    @PostMapping("/avatar/upload")
    CommonResult<UploadPictureDTO> uploadPicture(@RequestHeader("token") String token,
                                                 @RequestParam("avatar")MultipartFile avatar){
        CommonResult commonResult=new CommonResult();
        if(jwtUtil.isVerify(token)){
            UploadPictureDTO uploadPictureDTO=new UploadPictureDTO();
            //1.判断文件是否为图片类型
            String fileName=avatar.getOriginalFilename();
            fileName=fileName.toLowerCase(); //解决文件名后缀是大写的
            if(!fileName.matches("^.+\\.(jpg|png)$")){
                commonResult.setCode("2001");
                commonResult.setMessage("图片上传失败！");
                return commonResult;
            }
            //2.判断是否为恶意程序,转化成为图片对象
            try{
                BufferedImage bufferedImage=
                        ImageIO.read(avatar.getInputStream());
                int width=bufferedImage.getWidth();
                int height=bufferedImage.getHeight();
                if(width==0||height==0){
                    commonResult.setCode("2001");
                    commonResult.setMessage("图片恶意上传！");
                    return commonResult;
                }
                //3.实现份文件存储 按照yyyy/MM/dd
                String dateDir=new SimpleDateFormat("yyyy/MM/dd/")
                        .format(new Date());
                String fileDirPath=path+dateDir;
                File dirFile=new File(fileDirPath);
                if(!dirFile.exists()){
                    dirFile.mkdirs();
                }
                //4.生成文件防止重名 name.type
                int index=fileName.lastIndexOf(".");
                String fileType=fileName.substring(index);
                String uuid= UUID.randomUUID().toString();
                String realFileName=uuid+fileType;
                //5.文件实现上传
                File avatarFile=new File(fileDirPath+realFileName);
                avatar.transferTo(avatarFile);
                uploadPictureDTO.setWidth(width);
                uploadPictureDTO.setHeight(height);
                uploadPictureDTO.setUrl(dateDir + File.separatorChar + realFileName);
                String avatar_url=dateDir + File.separatorChar + realFileName;
                String user_name=jwtUtil.parseJWT(token);
                userMapper.updateAvatar_url(avatar_url,user_name);
                System.out.println(avatar_url);
                commonResult.setData(uploadPictureDTO);
                commonResult.setCode("6666");
                commonResult.setMessage("操作成功！");
            } catch (IOException e) {
                e.printStackTrace();
                commonResult.setCode("2001");
                commonResult.setMessage("图片上传失败！");
                return commonResult;
            }
        }
        commonResult.setCode("1006");
        commonResult.setMessage("用户无有效令牌！");
        return commonResult;
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
            String user_name=jwtUtil.parseJWT(token);
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
            String user_name=jwtUtil.parseJWT(token);
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
    CommonResult<UrlUUIDDTO> getCaptcha() {
        CommonResult commonResult=new CommonResult();
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(112, 38);
        try {
            String verification_code=lineCaptcha.getCode();
            String simpleUUID = IdUtil.simpleUUID();
            //文件实现上传
            String dateDir=new SimpleDateFormat("yyyy/MM/dd/")
                    .format(new Date());
            String fileDirPath=path+dateDir;
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
