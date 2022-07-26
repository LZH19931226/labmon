package com.hc.controller;

import com.hc.application.EquipmentInfoAppApplication;
import com.hc.dto.HospitalEquipmentDto;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/app")
public class AppController {

    @Autowired
    private EquipmentInfoAppApplication equipmentInfoAppApplication;

    @GetMapping("/getEquipmentNum")
    @ApiOperation("获取首页设备数量信息")
    public Map<String, HospitalEquipmentDto> getEquipmentNum(@Param("hospitalCode")String hospitalCode){
        return equipmentInfoAppApplication.getEquipmentNum(hospitalCode);
    }
}
