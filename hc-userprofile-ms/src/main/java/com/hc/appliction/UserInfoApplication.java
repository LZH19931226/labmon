package com.hc.appliction;

import com.hc.appliction.command.UserCommand;
import com.hc.service.UserBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户信息申请
 * @author hc
 */
@Component
public class UserInfoApplication {

    @Autowired
    private UserBackService userBackService;

    /**
     * 用户登录
     * @param userCommand 用户信息
     */
    public void userLogin(UserCommand userCommand) {
        userBackService.userLogin(userCommand);
    }

    /**
     * 修改密码
     * @param userCommand 用户信息
     */
    public void updatePassword(UserCommand userCommand) {
        userBackService.updatePassword(userCommand);
    }

}
