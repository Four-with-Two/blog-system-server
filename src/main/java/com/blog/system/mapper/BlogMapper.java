package com.blog.system.mapper;

import com.blog.system.model.Blog;
import org.apache.ibatis.annotations.*;

@Mapper
public interface BlogMapper {

    @Insert("insert into blog (author,title,content,summary,publish_date,update_date) values (#{author},#{title},#{content},#{summary},#{publish_date},#{update_date})")
    void insert(Blog blog);

    @Select("select * from blog where id=#{id}")
    Blog findByID(@Param("id") int id);

    @Delete("delete from blog where id=#{id}")
    void delete(@Param("id") int id);

    @Update("update blog set title=#{title},content=#{content},summary=#{summary},update_date=#{update_date} where id=#{id}")
    void update(Blog blog);
}
