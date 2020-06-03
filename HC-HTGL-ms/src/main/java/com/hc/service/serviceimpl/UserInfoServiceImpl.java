package com.hc.service.serviceimpl;

import com.hc.config.RedisTemplateUtil;
import com.hc.entity.Userback;
import com.hc.mapper.laboratoryFrom.UserInfoMapper;
import com.hc.service.UserInfoService;
import com.hc.units.ApiResponse;
import com.hc.units.TokenHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * Created by 16956 on 2018-08-05.
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoServiceImpl.class);
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;
    @Override
    public ApiResponse<String> userLogin(Userback userback) {
        ApiResponse<String> apiResponse = new ApiResponse<String>();
        Userback userback1 = new Userback();
        try{
            userback1 = userInfoMapper.userLogin(userback);
            if (StringUtils.isEmpty(userback1)){
                apiResponse.setCode(ApiResponse.FAILED);
                apiResponse.setMessage("登录失败，用户名或密码错误");
            }
            String username = userback.getUsername();
            //登录成功
            String token = TokenHelper.createToken(username+username);
            redisTemplateUtil.boundValueOps(username+username).set(token,1, TimeUnit.HOURS);
            apiResponse.setResult(token);
            return apiResponse;
        }catch(Exception e){
            LOGGER.error("失败：" +e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }

    }

    @Override
    public ApiResponse<String> updatePassword(Userback userback) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        Userback  userback1 = userInfoMapper.userLogin(userback);
        if (null == userback1){
            apiResponse.setCode(ApiResponse.FAILED);
            apiResponse.setMessage("用户名或者密码错误");
            return apiResponse;
        }
        userback1.setPwd(userback.getNewpwd());
        userInfoMapper.updatePwd(userback1);

        return apiResponse;
    }
}
