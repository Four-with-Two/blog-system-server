package com.blog.system.service;

import com.blog.system.dto.OtherDataDTO;
import com.blog.system.model.PersonalData;
import org.springframework.stereotype.Service;

@Service
public class OtherDataService {
    public OtherDataDTO getOtherData(PersonalData personalData){
        OtherDataDTO otherDataDTO=new OtherDataDTO();
        otherDataDTO.setUser_name(personalData.getUser_name());
        otherDataDTO.setNick_name(personalData.getNick_name());
        otherDataDTO.setGender(personalData.getGender());
        otherDataDTO.setProfile(personalData.getProfile());
        otherDataDTO.setMail(personalData.getMail());
        return otherDataDTO;
    }
}
