package com.hc.controller;

import com.hc.application.HospitalInfoRedisApplication;
import com.hc.my.common.core.redis.dto.HospitalInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户医院缓存i休尼希
 * @author hc
 */
@RestController
@RequestMapping("/hospitalRedis")
public class HospitalInfoRedisController {

    @Autowired
    private HospitalInfoRedisApplication hospitalInfoRedisApplication;

    /**
     * 获取医院信息
     * @param hospitalCode 医院id
     * @return 医院信息
     */
    @GetMapping("/findHospitalRedisInfo")
    public HospitalInfoDto  findHospitalRedisInfo(@RequestParam("hospitalCode")String hospitalCode){
        return hospitalInfoRedisApplication.findHospitalInfo(hospitalCode);
    }
}
