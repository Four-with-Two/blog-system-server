package com.blog.system.mapper;

import com.blog.system.model.Blog;
import org.apache.ibatis.annotations.*;

@Mapper
public interface BlogMapper {

    @Insert("insert into blog (title,essay,bio,release_time) values (#{title},#{essay},#{bio},#{release_time})")
    void insert(Blog blog);

    @Select("select * from blog where id=#{id}")
    Blog findByID(@Param("id") int id);

    @Delete("delete from blog where id=#{id}")
    void delete(@Param("id") int id);

    @Update("update blog set title=#{title},essay=#{essay},bio=#{bio} where id=#{id}")
    void update(Blog blog);
}
