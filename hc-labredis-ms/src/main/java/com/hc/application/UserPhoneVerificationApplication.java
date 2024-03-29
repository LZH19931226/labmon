package com.hc.application;

import com.hc.application.config.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserPhoneVerificationApplication {

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 获取断行验证码
     * @param phoneNum
     * @return
     */
    public String getPhoneCode(String phoneNum){
        Object o = redisUtils.get(phoneNum);
        return (String)o;
    }

    /**
     * 生成验证码并返回
     * @param phoneNum
     * @return
     */
    public void addPhoneCode(String phoneNum ,String code){
        redisUtils.set(phoneNum,code,120);
    }

}
