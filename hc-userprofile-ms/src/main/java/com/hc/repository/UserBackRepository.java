package com.hc.repository;

import com.hc.dto.UserBackDto;

/**
 *
 * @author hc
 */
public interface UserBackRepository {

    /**
     * 用户登录
     * @param username 用户名
     * @param pwd 密码
     * @return UserBackDto
     */
    UserBackDto userLogin(String username, String pwd);

    /**
     * 修改密码
     * @param userid 用户id
     * @param pwd 密码
     */
    void updatePassword(String userid, String pwd);
}
