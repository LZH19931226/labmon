package com.hc.appliction;

import com.hc.appliction.command.UserCommand;
import com.hc.command.labmanagement.model.UserBackModel;
import com.hc.dto.UserBackDto;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.service.UserBackService;
import com.hc.vo.user.UserInfoVo;
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
     *
     * @param userCommand 用户信息
     */
    public UserInfoVo userLogin(UserCommand userCommand) {
        UserBackDto userBackDto = userBackService.userLogin(userCommand);
        return UserInfoVo.builder()
                .username(userBackDto.getUsername())
                .userid(userBackDto.getUserid())
                .build();
    }

    /**
     * 修改密码
     *
     * @param userCommand 用户信息
     */
    public void updatePassword(UserCommand userCommand) {
        userBackService.updatePassword(userCommand);
    }

    /**
     * 获取用户信息
     *
     * @param userid
     * @return
     */
    public UserBackModel findUserInfo(String userid) {
        UserBackDto userBackDto = userBackService.selectUserBackByUserId(userid);
        return BeanConverter.convert(userBackDto, UserBackModel.class);
    }
}
