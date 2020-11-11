package com.blog.system.controller;

import com.blog.system.dto.KeyPassDTO;
import com.blog.system.dto.SendParamDTO;
import com.blog.system.mapper.PersonalDataMapper;
import com.blog.system.mapper.UserMapper;
import com.blog.system.model.PersonalData;
import com.blog.system.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/my_data")
public class PersonalDataController {

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private PersonalDataMapper personalDataMapper;

    /**
     * 初始化或修改密码
     * @param keyPassDTO
     * @return
     */
    @PatchMapping("/pwdAlteration")
    SendParamDTO pwdAlteration(@RequestBody KeyPassDTO keyPassDTO){
        User user=userMapper.findBySession_key(keyPassDTO.getSession_key());
        SendParamDTO CommonResult=new SendParamDTO();
        if(user!=null){
            userMapper.updateBySession_key(keyPassDTO.getSession_key(),keyPassDTO.getPassword());
            CommonResult.setCode("6666");
            CommonResult.setMessage("操作成功！");
            return CommonResult;
        }
        else{
            CommonResult.setCode("1003");
            CommonResult.setMessage("用户名不存在！");
            return CommonResult;
        }
    }

    /**
     * 获取其他用户的个人资料
     * @param token
     * @param user_name
     * @return
     */
    @GetMapping("/exhibition/{username}")
    SendParamDTO<PersonalData> showOtherData(@RequestHeader("token")String token,
                                             @PathVariable("username")String user_name){
        SendParamDTO CommonResult=new SendParamDTO();
        PersonalData personalData=personalDataMapper.findByUser_name(user_name);
        if(personalData!=null){
            CommonResult.setObject(personalData);
            CommonResult.setCode("6666");
            CommonResult.setMessage("操作成功！");
            return CommonResult;
        }
        else{
            CommonResult.setCode("1003");
            CommonResult.setMessage("用户名不存在！");
            return CommonResult;
        }
    }

    /**
     * 获取自己的个人资料
     * @param keyPassDTO
     * @return
     */
    @GetMapping("/exhibition")
    SendParamDTO<PersonalData> showMyData(@RequestBody KeyPassDTO keyPassDTO){
        SendParamDTO CommonResult=new SendParamDTO();
        PersonalData personalData=personalDataMapper.findBySession_key(keyPassDTO.getSession_key());
        if(personalData!=null){
            CommonResult.setObject(personalData);
            CommonResult.setCode("6666");
            CommonResult.setMessage("操作成功！");
            return CommonResult;
        }
        else{
            CommonResult.setCode("1003");
            CommonResult.setMessage("用户名不存在！");
            return CommonResult;
        }
    }

    @PutMapping("/alteration")
    SendParamDTO alteration(@RequestBody PersonalData personalData){
        SendParamDTO CommonResult=new SendParamDTO();
        PersonalData personalData2=personalDataMapper.findByUser_name(personalData.getUser_name());
        if(personalData!=null){
            CommonResult.setCode("6666");
            CommonResult.setMessage("操作成功！");
            return CommonResult;
        }
        else{
            CommonResult.setCode("1003");
            CommonResult.setMessage("用户名不存在！");
            return CommonResult;
        }
    }
}
