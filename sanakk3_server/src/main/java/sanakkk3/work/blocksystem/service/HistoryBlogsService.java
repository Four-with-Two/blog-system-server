package sanakkk3.work.blocksystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sanakkk3.work.blocksystem.mapper.HistoryBlogMapper;
import sanakkk3.work.blocksystem.model.HistoryBlog;

import java.util.List;

@Service
public class HistoryBlogsService {

    @Autowired(required = false)
    private HistoryBlogMapper historyBlogMapper;

    /**
     * 生成历史博客列表
     * @param username
     * @return
     */
    public List<HistoryBlog> list(String username) {
        List<HistoryBlog> historyBlogs1=historyBlogMapper.list();
        List<HistoryBlog> historyBlogs2=null;
        for(HistoryBlog historyBlog:historyBlogs1){
            if(historyBlog.getAuthor_name()==username){
                historyBlogs2.add(historyBlog);
            }
        }
        return historyBlogs2;
    }
}
