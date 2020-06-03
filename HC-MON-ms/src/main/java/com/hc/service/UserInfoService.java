package com.hc.service;

import com.hc.model.LoginResponseModel;
import com.hc.model.UserModel;
import com.hc.utils.ApiResponse;

/**
 * Created by 16956 on 2018-07-27.
 */
public interface UserInfoService {

    /**
     * 用户登录/App  -- web
     */
    ApiResponse<LoginResponseModel> userLoginByApp(UserModel userModel);

    /**
     * 用户登录安卓
     */
    ApiResponse<LoginResponseModel> userLoginByAndrio(UserModel userModel);

    /**
     * 用户登录安卓
     */
    ApiResponse<LoginResponseModel> userLoginByAndrios(UserModel userModel);
    /**
     * 发送验证码
     */
    ApiResponse<String> getCode(String phonenum);

    /**
     * 修改手机号
     */
    ApiResponse<String> updatePhone(UserModel userModel);

    /**
     * 修改密码
     */
    ApiResponse<String> updatePwd(UserModel userModel);

    /**
     * 退出登录
     */
    ApiResponse<String> loginOut(String token);



}
