package sanakkk3.work.blocksystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sanakkk3.work.blocksystem.dto.MesDTO;
import sanakkk3.work.blocksystem.mapper.HotBlogMapper;
import sanakkk3.work.blocksystem.model.HotBlog;

import java.util.List;

@RestController
@RequestMapping("/home")
public class HotBlogsController {

    @Autowired(required = false)
    private HotBlogMapper hotBlogMapper;
    /**
     * 查看全部历史博客
     * @return
     */
    @GetMapping("/hotBlogs")
    public MesDTO<List<HotBlog>> HotBlogs(@RequestParam("sessionKey") String sessionKey){
        List<HotBlog> list = null;
        MesDTO CommonResult = new MesDTO();
        CommonResult.setData(list);
        CommonResult.setCode("6666");
        CommonResult.setMessage("成功");
        return CommonResult;
    }
}
