package com.hc.appliction;

import com.hc.dto.UserBackDto;
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
        UserBackDto userBackDto = new UserBackDto().setUsername(userCommand.getUsername()).setPwd(userCommand.getPwd());
        userBackService.userLogin(userBackDto);
    }

    public void updatePassword(UserCommand userCommand) {
        UserBackDto userBackDto = new UserBackDto().setUsername(userCommand.getUsername()).setPwd(userCommand.getPwd());
        userBackService.updatePassword(userBackDto);
    }

}
