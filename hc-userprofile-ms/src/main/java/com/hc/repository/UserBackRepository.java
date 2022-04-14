package com.hc.repository;

import com.hc.dto.UserBackDto;

/**
 *
 * @author hc
 */
public interface UserBackRepository {

    /**
     * 用户登录
     * @param userBackDto 数据传输对象
     * @return UserBackDto
     */
    UserBackDto userLogin(UserBackDto userBackDto);

    /**
     * 修改密码
     * @param userBackDto 数据传输对象
     */
    void updatePassword(UserBackDto userBackDto);
}
