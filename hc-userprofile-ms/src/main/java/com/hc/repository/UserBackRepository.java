package com.hc.repository;

import com.hc.dto.UserBackDto;
import com.hc.po.UserBackPo;

/**
 *
 * @author hc
 */
public interface UserBackRepository {

    /**
     * 用户登录
     * @param userBackPo 数据传输对象
     * @return UserBackDto
     */
    UserBackDto userLogin(UserBackPo userBackPo);

    /**
     * 修改密码
     * @param userBackPo 数据传输对象
     */
    void updatePassword(UserBackPo userBackPo);

    /**
     * 根据用户id查询user信息
     * @param userId
     * @return
     */
    UserBackDto selectUserBackByUserId(String userId);
}
