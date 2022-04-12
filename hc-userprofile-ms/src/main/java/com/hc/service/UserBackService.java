package com.hc.service;

import com.hc.dto.UserBackDto;

/**
 * @author hc
 */
public interface UserBackService {

    /**
     * 用户登录
     * @param username 用户
     * @param pwd
     * @return
     */
    UserBackDto userLogin(String username, String pwd);

    /**
     * 修改密码
     * @param userid 用户id
     * @param pwd 密码
     */
    void updatePassword(String userid, String pwd);
}
