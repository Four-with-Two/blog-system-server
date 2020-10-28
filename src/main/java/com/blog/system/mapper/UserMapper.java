package com.blog.system.mapper;

import com.blog.system.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("select * from user where mail=#{mail}")
    User findByMail(@Param("mail") String mail);

    @Select("select * from user where id=#{id}")
    User findByID(@Param("id") int id);

    @Insert("insert into user (mail,user_password) values (#{mail},#{user_password})")
    void insertUser(@Param("mail") String mail,@Param("user_password") String user_password);
}
