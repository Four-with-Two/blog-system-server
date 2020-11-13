package com.blog.system.controller;

import com.blog.system.dto.*;
import com.blog.system.mapper.PersonalDataMapper;
import com.blog.system.mapper.UserMapper;
import com.blog.system.model.PersonalData;
import com.blog.system.model.User;
import com.blog.system.service.OtherDataService;
import com.blog.system.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/my_data")
public class PersonalDataController {

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private PersonalDataMapper personalDataMapper;

    @Autowired
    private OtherDataService otherDataService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 初始化或修改密码
     * @param token
     * @param password
     * @return
     */
    @PatchMapping("/pwdAlteration")
    CommonResult pwdAlteration(@RequestHeader("token") String token,
                               @RequestBody String password){
        CommonResult commonResult=new CommonResult();

//        System.out.println("token:"+token);
//        System.out.println("password:"+password);
//        commonResult.setCode("6666");
//        commonResult.setMessage("操作成功！");
//        return commonResult;

        if(jwtUtil.isVerify(token)==true){
            String user_name=jwtUtil.parseJWT(token);
            User user=userMapper.findByUser_name(user_name);
            userMapper.updateByUser_name(user_name,password);
            long ttlMillis=System.currentTimeMillis();
            String newToken=jwtUtil.createJWT(ttlMillis,user);
            commonResult.setData(newToken);
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
     * 获取其他用户的个人资料
     * @param token
     * @param user_name
     * @return
     */
    @GetMapping("/exhibition/{username}")
    CommonResult<OtherDataDTO> showOtherData(@RequestHeader("token")String token,
                                             @PathVariable("username") String user_name){
        CommonResult commonResult=new CommonResult();

//        System.out.println("token:"+token);
//        System.out.println("user_name:"+user_name);
//        OtherDataDTO otherDataDTO=new OtherDataDTO();
//        otherDataDTO.setUser_name("sana");
//        otherDataDTO.setNick_name("sai");
//        otherDataDTO.setGender("girl");
//        otherDataDTO.setProfile("你好！阿巴阿巴...");
//        otherDataDTO.setMail("123456@qq.com");
//        commonResult.setData(otherDataDTO);
//        commonResult.setCode("6666");
//        commonResult.setMessage("操作成功！");
//        return commonResult;

        if(jwtUtil.isVerify(token)==true){
            PersonalData personalData=personalDataMapper.findByUser_name(user_name);
            if(personalData!=null){
                OtherDataDTO otherDataDTO =new OtherDataDTO();
                otherDataDTO=otherDataService.getOtherData(personalData);
                commonResult.setData(otherDataDTO);
                commonResult.setCode("6666");
                commonResult.setMessage("操作成功！");
                return commonResult;
            }
            else{
                commonResult.setCode("1003");
                commonResult.setMessage("用户名不存在！");
                return commonResult;
            }
        }
        else{
            commonResult.setCode("1006");
            commonResult.setMessage("用户无有效令牌!");
            return commonResult;
        }
    }

    /**
     *  获取自己的个人资料
     * @param token
     * @return
     */
    @GetMapping("/exhibition")
    CommonResult<PersonalData> showMyData(@RequestHeader("token")String token){
        CommonResult commonResult=new CommonResult();

//        System.out.println("token:"+token);
//        PersonalData personalData=new PersonalData();
//        personalData.setUser_name("sana");
//        personalData.setNick_name("sai");
//        personalData.setGender("girl");
//        personalData.setBirthday("2000-12-12");
//        personalData.setMail("12345@qq.com");
//        personalData.setPhone("123456");
//        personalData.setProfile("你好！阿巴阿巴...");
//        commonResult.setData(personalData);
//        commonResult.setCode("6666");
//        commonResult.setMessage("操作成功！");
//        return commonResult;

        if(jwtUtil.isVerify(token)){
            String user_name=jwtUtil.parseJWT(token);
            PersonalData personalData=personalDataMapper.findByUser_name(user_name);
            if(personalData!=null){
                commonResult.setData(personalData);
                commonResult.setCode("6666");
                commonResult.setMessage("操作成功！");
                return commonResult;
            }
            else{
                commonResult.setCode("1003");
                commonResult.setMessage("用户名不存在！");
                return commonResult;
            }
        }
        else{
            commonResult.setCode("1006");
            commonResult.setMessage("用户无有效令牌!");
            return commonResult;
        }
    }

    /**
     * 修改个人资料
     * @param personalData
     * @return
     */
    @PutMapping("/alteration")
    CommonResult alteration(@RequestHeader("token")String token,
                            @RequestBody PersonalData personalData){
        CommonResult commonResult=new CommonResult();

//        System.out.println("personalData:"+personalData);
//        commonResult.setCode("6666");
//        commonResult.setMessage("操作成功！");
//        return commonResult;

        if(jwtUtil.isVerify(token)){
            PersonalData personalData1=personalDataMapper.findByUser_name(jwtUtil.parseJWT(token));
            if(personalData!=null){
                personalDataMapper.updatePersonalData(personalData);
                userMapper.updataData(personalData);
                commonResult.setCode("6666");
                commonResult.setMessage("操作成功！");
                return commonResult;
            }
            else{
                commonResult.setCode("1003");
                commonResult.setMessage("用户名不存在！");
                return commonResult;
            }
        }
        else{
            commonResult.setCode("1006");
            commonResult.setMessage("用户无有效令牌!");
            return commonResult;
        }
    }
}
