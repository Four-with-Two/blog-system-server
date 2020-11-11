package com.blog.system.mapper;

import com.blog.system.model.PersonalData;
import lombok.Data;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PersonalDataMapper {

    @Select("select * from personal_data where user_name=#{user_name}")
    PersonalData findByUser_name(@Param("user_name")String user_name);

    @Select("select * from personal_data where user_name=#{user_name}")
    PersonalData findBySession_key(@Param("session_key")String user_name);
}
