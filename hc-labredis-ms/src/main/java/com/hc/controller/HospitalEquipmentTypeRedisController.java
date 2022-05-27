package com.hc.controller;

import com.hc.application.HospitalEquipmentTypeRedisApplication;
import com.hc.my.common.core.redis.dto.HospitalEquipmentTypeInfoDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 医院设备缓存控制层
 */
@RestController
@RequestMapping("/hospitalEquipmentType")
public class HospitalEquipmentTypeRedisController {

    @Autowired
    private HospitalEquipmentTypeRedisApplication hospitalEquipmentTypeRedisApplication;

    /**
     * 获取医院设备类型缓存信息
     * @param hospitalCode 医院id
     * @param hospitalEquipmentTypeId 医院设备类型id
     * @return 医院设备类型缓存信息
     */
    @GetMapping("/getHospitalEquipmentTypeRedisInfo")
    @ApiOperation("获取医院设备类型缓存信息")
    public HospitalEquipmentTypeInfoDto findHospitalEquipmentTypeRedisInfo(@RequestParam("hospitalCode")String hospitalCode,
                                                                           @RequestParam("hospitalEquipmentTypeId")String hospitalEquipmentTypeId){
        return hospitalEquipmentTypeRedisApplication.findHospitalEquipmentTypeRedisInfo(hospitalCode,hospitalEquipmentTypeId);
    }

    /**
     * 新增或修改医院设备类型缓存信息
     * @param hospitalEquipmentTypeInfoModel 医院设备类型缓存信息
     */
    @PostMapping("/addHospitalEquipmentTypeRedisInfo")
    @ApiOperation("新增或修改医院设备类型缓存信息")
    public void addHospitalEquipmentTypeRedisInfo(@RequestBody HospitalEquipmentTypeInfoDto hospitalEquipmentTypeInfoModel){
        hospitalEquipmentTypeRedisApplication.addHospitalEquipmentTypeRedisInfo(hospitalEquipmentTypeInfoModel);
    }

    /**
     * 移除医院设备类型缓存信息
     * @param hospitalCode 医院id
     * @param hospitalEquipmentTypeId 医院设备类型id
     */
    @DeleteMapping("/removeHospitalEquipmentTypeRedisInfo")
    @ApiOperation("移除医院设备类型缓存信息")
    public void removeHospitalEquipmentTypeRedisInfo(@RequestParam("hospitalCode")String hospitalCode,
                                                     @RequestParam("hospitalEquipmentTypeId")String hospitalEquipmentTypeId){
        hospitalEquipmentTypeRedisApplication.removeHospitalEquipmentTypeRedisInfo(hospitalCode,hospitalEquipmentTypeId);
    }

    /**
     * 同步所有的设备类型信息
     */
    @GetMapping("/cacheHospitalEquipmentTypeRedisInfo")
    @ApiOperation("同步所有的设备类型信息")
    public void hospitalEquipmentTypeRedisInfoCache(){
        hospitalEquipmentTypeRedisApplication.hospitalEquipmentTypeRedisInfoCache();
    }
}
