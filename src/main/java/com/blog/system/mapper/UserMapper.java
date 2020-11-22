package com.blog.system.mapper;

import com.blog.system.model.PersonalData;
import com.blog.system.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("select avatar_url from user where user_name=#{user_name}")
    String getAvatar(@Param("user_name") String user_name);

    @Select("select * from user where id=#{id}")
    User findByID(@Param("id") int id);

    @Select("select * from user where mail=#{mail}")
    User findByMail(@Param("mail") String mail);

    @Select("Select * from user where user_name=#{user_name}")
    User findByUser_name(@Param("user_name") String user_name);

    @Insert("insert into user (user_name,mail,password) values (#{user_name},#{mail},#{password})")
    void insertUser(User user);

    @Update("update user set avatar_url=#{avatar_url} where user_name=#{user_name}")
    void updateAvatar_url(@Param("avatar_url") String avatar_url, @Param("user_name") String user_name);

    @Update("update user set password=#{password} where user_name=#{user_name}")
    void  updateByUser_name(@Param("user_name") String user_name,@Param("password")String password);

    @Update("update user set nick_name=#{nick_name},gender=#{gender},mail=#{mail},birthday=#{birthday}," +
            "phone=#{phone},bio=#{profile}" +
            "where user_name=#{user_name}")
    void updateData(PersonalData personalData);

    @Update("update user set avatar_url=#{avatar_url} where user_name=#{user_name}")
    void updatePicture(@Param("avatar_url")String avatar_url,@Param("user_name")String user_name);

    @Delete("delete from user where user_name=#{user_name}")
    void deleteByUser_name(@Param("user_name") String user_name);
}
