package com.hc.controller;

import com.hc.application.UserPhoneVerificationApplication;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/phone")
@Slf4j
public class UserPhoneVerificationController {

    @Autowired
    private UserPhoneVerificationApplication userPhoneVerificationApplication;

    /**
     * 获取手机验证码
     * @param phoneNum
     * @return
     */
    @GetMapping("/getPhoneCode")
    @ApiOperation("获取手机验证码")
    private String getPhoneCode(@RequestParam("phoneNum") String phoneNum){
        return userPhoneVerificationApplication.getPhoneCode(phoneNum);
    }

    @GetMapping("/addCode")
    @ApiOperation("生成手机验证码")
    public void addPhoneCode(@RequestParam("phoneNum")String phoneNum ,@RequestParam("code")String code){
        log.info("生成短信验证码:{}",phoneNum+code);
         userPhoneVerificationApplication.addPhoneCode(phoneNum,code);
    }
}
