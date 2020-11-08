package com.blog.system.mapper;

import com.blog.system.model.Blog;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BlogMapper {

    @Insert("insert into blog (author,title,content,summary,publish_date,update_date) values (#{author},#{title},#{content},#{summary},#{publish_date},#{update_date})")
    void insert(Blog blog);

    @Select("select * from blog where id=#{id}")
    Blog findByID(@Param("id") int id);

    @Select("select count(*) from blog where author=#{author}")
    int countByAuthorID(@Param("author") int author);

    @Select("select count(*) from blog")
    int countAll();

    @Select("select * from blog where author=#{author} limit (#{page}-1)*#{size},#{size} order by publish_date desc")
    List<Blog> findByAuthorID(@Param("author") int author,@Param("page") int page,@Param("size") int size);

    @Select("select * from blog limit (#{page}-1)*#{size},#{size} order by publish_date desc")
    List<Blog> findAll(@Param("page") int page,@Param("size") int size);

    @Delete("delete from blog where id=#{id}")
    void delete(@Param("id") int id);

    @Update("update blog set title=#{title},content=#{content},summary=#{summary},update_date=#{update_date} where id=#{id}")
    void update(Blog blog);
}
