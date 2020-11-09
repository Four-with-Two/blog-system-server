package sanakkk3.work.blocksystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sanakkk3.work.blocksystem.dto.MesDTO;
import sanakkk3.work.blocksystem.mapper.MeUserMesMapper;
import sanakkk3.work.blocksystem.mapper.PersonalDataMapper;
import sanakkk3.work.blocksystem.mapper.UserMapper;
import sanakkk3.work.blocksystem.mapper.UserMesMapper;
import sanakkk3.work.blocksystem.model.HistoryBlog;
import sanakkk3.work.blocksystem.model.MeUserMes;
import sanakkk3.work.blocksystem.model.PersonalData;
import sanakkk3.work.blocksystem.model.UserMes;
import sanakkk3.work.blocksystem.service.HistoryBlogsService;

import java.util.List;

@RestController
@RequestMapping("/my_Data")
public class UserMseController {

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private UserMesMapper userMesMapper;

    @Autowired(required = false)
    private MeUserMesMapper meUserMesMapper;

    @Autowired(required = false)
    private PersonalDataMapper personalDataMapper;

    @Autowired
    private HistoryBlogsService historyBlogsService;

    /**
     *修改密码
     * @param sessionKey
     * @param password
     * @return
     */
    @PatchMapping("/pwdAlteration")
    public MesDTO pwdAlteration(@RequestParam("sessionKey") String sessionKey,
                                @RequestParam("password") String password){
        userMapper.changePassword(password,sessionKey);
        MesDTO CommonResult =new MesDTO();
        CommonResult.setCode("6666");
        CommonResult.setMessage("操作成功！");
        return CommonResult;
    }

    /**
     * 获取其他用户的个人资料（文字）
     * @param token
     * @param username
     * @return
     */
    @GetMapping("/exhibition/{username}")
    public MesDTO<UserMes> exhibitionOtherUser(@RequestParam("token") String token,
                                               @PathVariable("username") String username){
        UserMes userMes=new UserMes();
        userMes=userMesMapper.findByUserName(username);
        MesDTO CommonResult =new MesDTO();
        if(userMes!=null){
            CommonResult.setData(userMes);
            CommonResult.setCode("6666");
            CommonResult.setMessage("操作成功！");
            return CommonResult;
        }
        else{
            CommonResult.setCode("1003");
            CommonResult.setMessage("用户名不存在！");
            return CommonResult;
        }
    }

    /**
     * 获取自己的个人资料（文字）
     * @param sessionKey
     * @return
     */
    @GetMapping("/exhibition")
    public MesDTO<MeUserMes> exhibitionMeUser(@RequestParam("sessionKey") String sessionKey){

        MeUserMes meUserMes=new MeUserMes();
        meUserMes=meUserMesMapper.findByUserName(sessionKey);
        MesDTO CommonResult=new MesDTO();
        if(meUserMes!=null){
            CommonResult.setData(meUserMes);
            CommonResult.setCode("6666");
            CommonResult.setMessage("操作成功！");
            return CommonResult;
        }
       else{
           CommonResult.setCode("1003");
           CommonResult.setMessage("用户名不存在！");
           return CommonResult;
        }
    }

    /**
     * 查看全部历史博客
     * @param sessionKey
     * @param username
     * @return
     */
    @GetMapping("/blog/{username}/all")
    public MesDTO<List<HistoryBlog>> historyBlogs(@RequestParam("sessionkey") String sessionKey,
                                                  @PathVariable("username") String username){
        List<HistoryBlog> historyBlogList=null;
        historyBlogList=historyBlogsService.list(username);
        MesDTO CommonResult=new MesDTO();
        CommonResult.setData(historyBlogList);
        CommonResult.setCode("6666");
        CommonResult.setMessage("操作成功！");
        return CommonResult;
    }

    /**
     * 修改个人资料（文字）
     * @param personalData
     * @return
     */
    @PutMapping("/alternation")
    public MesDTO alternation(@RequestBody PersonalData personalData){
        personalDataMapper.updatePersonalData(personalData);
        MesDTO CommonResult=new MesDTO();
        CommonResult.setCode("6666");
        CommonResult.setMessage("操作成功！");
        return CommonResult;
    }
}
