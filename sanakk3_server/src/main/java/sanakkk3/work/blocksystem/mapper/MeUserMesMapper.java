package sanakkk3.work.blocksystem.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import sanakkk3.work.blocksystem.model.MeUserMes;

@Mapper
public interface MeUserMesMapper {

    /**
     * 获取自己的个人资料（文字）
     * @param sessionKey
     * @return
     */
    @Select("select * from me_user_mes where user_name=#{sessionKey}")
    MeUserMes findByUserName(@Param("sessionKey") String sessionKey);
}
