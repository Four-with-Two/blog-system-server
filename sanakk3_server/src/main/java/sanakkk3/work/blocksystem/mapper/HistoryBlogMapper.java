package sanakkk3.work.blocksystem.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import sanakkk3.work.blocksystem.model.HistoryBlog;

import java.util.List;

@Mapper
public interface HistoryBlogMapper {

    /**
     * 获取所有的历史博客
     * @return
     */
    @Select("select * from history_blog")
    public List<HistoryBlog> list();

}
