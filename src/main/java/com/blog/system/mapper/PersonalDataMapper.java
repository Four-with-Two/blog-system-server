package com.blog.system.mapper;

import com.blog.system.model.PersonalData;
import com.blog.system.model.User;
import lombok.Data;
import org.apache.ibatis.annotations.*;

@Mapper
public interface PersonalDataMapper {

    @Select("select * from personal_data where user_name=#{user_name}")
    PersonalData findByUser_name(@Param("user_name")String user_name);

    @Update("update table personal_data set (nick_name,gender,profile,mail,birthday,phone) " +
            "values (#{nick_name},#{gender}),#{profile},#{mail},#{birthday},#{phone}" +
            "where user_name=#{user_name}" )
    void updatePersonalData(PersonalData personalData);

    @Insert("insert into personal_data set (user_name,nick_name,gender,mail,birthday,phone) " +
            "values(#{user_name},#{nick_name},#{gender},#{mail},#{birthday},#{phone})")
    void insertUser(User user);

    @Delete("delete from personal_data where user_name=#{user_name}")
    void delete(@Param("user_name")String user_name);
}
