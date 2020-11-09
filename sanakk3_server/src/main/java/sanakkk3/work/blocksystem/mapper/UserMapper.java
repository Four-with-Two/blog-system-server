package sanakkk3.work.blocksystem.mapper;

import lombok.Data;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import sanakkk3.work.blocksystem.model.UserMes;

@Mapper
public interface UserMapper {

    /**
     * 修改密码
     * @param password
     * @param sessionKey
     */
    @Update("update table user set user_password=#{password} where user_name=#{sessionKey}")
    void changePassword(@Param("password") String password,@Param("sessionKey") String sessionKey);

}
