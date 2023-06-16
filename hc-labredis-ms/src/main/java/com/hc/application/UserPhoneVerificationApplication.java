package com.hc.application;

import com.hc.application.config.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
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
        log.info("获取短信验证码:{}",phoneNum);
        return (String)o;
    }

    /**
     * 生成验证码并返回
     * @param phoneNum
     * @return
     */
    public void addPhoneCode(String phoneNum ,String code){
        redisUtils.set(phoneNum,code,120);
        log.info("生成短信验证码:{}",phoneNum+code);
    }

}
