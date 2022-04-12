package com.hc.appliction;

import com.hc.service.UserBackService;
import com.hc.vo.User.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户登录应用层
 * @author hc
 */
@Component
public class UserInfoApplication {

    @Autowired
    private UserBackService userBackService;

    public void userLogin(UserInfoVo userInfoVo) {
        userBackService.userLogin(userInfoVo.getUsername(),userInfoVo.getPwd());
    }

    public void updatePassword(UserInfoVo userInfoVo) {
        userBackService.updatePassword(userInfoVo.getUserid(),userInfoVo.getPwd());
    }
}
