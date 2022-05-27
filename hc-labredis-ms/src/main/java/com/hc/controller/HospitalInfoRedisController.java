package com.hc.controller;

import com.hc.application.HospitalInfoRedisApplication;
import com.hc.my.common.core.redis.dto.HospitalInfoDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户医院缓存控制层
 * @author hc
 */
@RestController
@RequestMapping("/hospitalRedis")
public class HospitalInfoRedisController {

    @Autowired
    private HospitalInfoRedisApplication hospitalInfoRedisApplication;

    /**
     * 获取医院缓存信息
     * @param hospitalCode 医院id
     * @return 医院信息
     */
    @GetMapping("/findHospitalRedisInfo")
    @ApiOperation("获取医院缓存信息")
    public HospitalInfoDto  findHospitalRedisInfo(@RequestParam("hospitalCode")String hospitalCode){
        return hospitalInfoRedisApplication.findHospitalInfo(hospitalCode);
    }

    /**
     * 新增医院缓存信息
     * @param hospitalInfoDto 医院缓存对象
     */
    @PostMapping("/insertHospitalRedisInfo")
    @ApiOperation("新增医院缓存信息")
    public void addHospitalRedisInfo(@RequestBody HospitalInfoDto hospitalInfoDto){
        hospitalInfoRedisApplication.addHospitalRedisInfo(hospitalInfoDto);
    }

    /**
     * 移除医院缓存信息
     * @param hospitalCode 医院id
     */
    @DeleteMapping("/removeHospitalRedisInfo")
    @ApiOperation("移除医院缓存信息")
    public void removeHospitalRedisInfo(@RequestParam("hospitalCode")String hospitalCode){
        hospitalInfoRedisApplication.removeHospitalRedisInfo(hospitalCode);
    }

    /**
     * 同步医院信息
     */
    @GetMapping("/hospitalInfoCache")
    @ApiOperation("同步医院信息")
    public void hospitalInfoCache(){
        hospitalInfoRedisApplication.hospitalInfoCache();
    }
}
