package com.hc.service;

import com.hc.appliction.command.UserCommand;
import com.hc.dto.UserBackDto;

/**
 * @author hc
 */
public interface UserBackService {

    /**
     * 用户登录
     *
     * @param userBackDto
     * @return
     */
    UserBackDto userLogin(UserCommand userBackDto);

    /**
     * 修改密码
     * @param userBackDto 用户数据传输对象
     */
    void updatePassword(UserCommand userBackDto);
}
