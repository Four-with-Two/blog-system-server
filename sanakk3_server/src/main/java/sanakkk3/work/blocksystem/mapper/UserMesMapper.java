package sanakkk3.work.blocksystem.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import sanakkk3.work.blocksystem.model.UserMes;

@Mapper
public interface UserMesMapper {
    /**
     * 获取其他用户的个人资料（文字）
     * @param username
     * @return
     */
    @Select("select * from user_mes where user_name=#{username}")
    UserMes findByUserName(@Param("username") String username);
}
