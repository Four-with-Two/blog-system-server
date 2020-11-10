package com.blog.system.mapper;

import com.blog.system.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Select("select * from user where id=#{id}")
    User findByID(@Param("id") int id);

    @Select("select * from user where mail=#{mail}")
    User findByMail(@Param("mail") String mail);

    @Select("Select * from user where user_name=#{user_name}")
    User findByUser_name(@Param("user_name") String user_name);

    @Insert("insert into user (user_name,mail,password) values (#{user_name},#{mail},#{password})")
    void insertUser(User user);

    @Delete("delete from user where id=#{id}")
    void delete(@Param("id") int id);
}
