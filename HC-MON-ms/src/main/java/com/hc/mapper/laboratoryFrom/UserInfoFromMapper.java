package com.hc.mapper.laboratoryFrom;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.hc.entity.Userright;
import com.hc.model.UserModel;

/**
 * Created by 16956 on 2018-07-31.
 */
@Mapper
@Component
public interface UserInfoFromMapper {

    /**
     * 登录验证
     */
    @Select("select * from userright where username = #{username} and pwd = #{pwd} and isuse = '1'")
    Userright userlogin(UserModel userModel);
}
