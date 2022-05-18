package com.hc.controller;

import com.hc.application.EquipmentInfoApplication;
import com.hc.dto.MonitorEquipmentDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/equipmentInfo")
public class EquipmentInfoController {

    @Autowired
    private EquipmentInfoApplication equipmentInfoApplication;


    @GetMapping("/getEquipmentCurrentData")
    @ApiOperation("查询所有设备当前值信息")
    public List<MonitorEquipmentDto> getEquipmentCurrentData(@RequestParam("hospitalCode")String hospitalCode,
                                                             @RequestParam("equipmentTypeId")String equipmentTypeId){
        return equipmentInfoApplication.findEquipmentCurrentData(hospitalCode,equipmentTypeId);
    }

}
