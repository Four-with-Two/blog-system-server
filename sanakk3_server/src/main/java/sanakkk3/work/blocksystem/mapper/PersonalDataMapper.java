package sanakkk3.work.blocksystem.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import sanakkk3.work.blocksystem.model.PersonalData;

@Mapper
public interface PersonalDataMapper {

    /**
     * 修改个人资料（文字）
     * @param personalData
     */
    @Update("update table personal_data set session_key=#{sessionKey},user_name=#{username},nick_name=#{nick_name},gender=#{gender},profile=#{profile},mail=#{mail},birthday=#{birthday},phone=#{phone}")
    void updatePersonalData(PersonalData personalData);
}
