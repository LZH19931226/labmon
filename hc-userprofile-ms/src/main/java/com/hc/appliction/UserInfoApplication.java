package com.hc.appliction;

import com.hc.appliction.command.UserCommand;
import com.hc.service.UserBackService;
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

    public void userLogin(UserCommand userCommand) {
        userBackService.userLogin(userCommand);
    }

    public void updatePassword(UserCommand userCommand) {
        userBackService.updatePassword(userCommand);
    }

}
