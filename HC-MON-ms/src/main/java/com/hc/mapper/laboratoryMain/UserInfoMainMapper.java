package com.hc.mapper.laboratoryMain;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.hc.po.Userright;
import com.hc.model.UserModel;

/**
 * Created by 16956 on 2018-07-31.
 */
@Mapper
@Component
public interface UserInfoMainMapper {

    /**
     * 更改用户信息：APP登录
     */
    @Select("select * from userright where userid = #{userid} and pwd = #{pwd} and isuse = '1'")
    Userright userlogin(UserModel userModel);


}
