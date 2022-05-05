package com.hc.mapper.laboratoryFrom;

import com.hc.po.Userback;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * Created by 16956 on 2018-08-05.
 */
@Mapper
@Component
public interface UserInfoMapper {

    @Select("select * from UserBack where username = #{username} and pwd = #{pwd}")
    Userback userLogin(Userback userback);

    @Select(" update UserBack set pwd = #{pwd} where username = #{username} ")
    void updatePwd(Userback userback);

}

