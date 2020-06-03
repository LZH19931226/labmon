package com.hc.service;

import com.hc.entity.Userback;
import com.hc.units.ApiResponse;

/**
 * Created by 16956 on 2018-08-05.
 */
public interface UserInfoService {

    /**
     * 登录
     */
    ApiResponse<String> userLogin(Userback userback);

    /**
     * 修改密码
     */
    ApiResponse<String> updatePassword(Userback userback);

}
