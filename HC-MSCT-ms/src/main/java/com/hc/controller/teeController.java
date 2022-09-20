package com.hc.controller;

import cn.hutool.json.JSONUtil;
import com.hc.my.common.core.redis.dto.UserRightRedisDto;
import com.hc.service.AlmMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class teeController {


    @Autowired
    private AlmMsgService almMsgService;

    @GetMapping("/TEST")
    public void test(String hosId){
        List<UserRightRedisDto> userList =almMsgService.addUserScheduLing(hosId);
        System.out.println(JSONUtil.toJsonStr(userList));
    }
}
