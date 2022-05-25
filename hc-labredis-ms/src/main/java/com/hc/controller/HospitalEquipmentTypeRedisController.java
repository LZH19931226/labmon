package com.hc.controller;

import com.hc.application.HospitalEquipmentTypeRedisApplication;
import com.hc.my.common.core.redis.dto.HospitalEquipmentTypeInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public HospitalEquipmentTypeInfoModel findHospitalEquipmentTypeRedisInfo(@RequestParam("hospitalCode")String hospitalCode,
                                                                             @RequestParam("hospitalEquipmentTypeId")String hospitalEquipmentTypeId){
        return hospitalEquipmentTypeRedisApplication.findHospitalEquipmentTypeRedisInfo(hospitalCode,hospitalEquipmentTypeId);
    }
}
