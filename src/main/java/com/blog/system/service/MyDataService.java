package com.blog.system.service;

import com.blog.system.dto.MyDataDTO;
import com.blog.system.dto.OtherDataDTO;
import com.blog.system.model.PersonalData;
import org.springframework.stereotype.Service;

@Service
public class MyDataService {
    public MyDataDTO getMyData(PersonalData personalData){
        MyDataDTO myDataDTO=new MyDataDTO();
        myDataDTO.setUser_name(personalData.getUser_name());
        myDataDTO.setNick_name(personalData.getNick_name());
        myDataDTO.setGender(personalData.getGender());
        myDataDTO.setProfile(personalData.getProfile());
        myDataDTO.setMail(personalData.getMail());
        myDataDTO.setBirthday(personalData.getBirthday());
        myDataDTO.setPhone(personalData.getPhone());
        return myDataDTO;
    }
}
