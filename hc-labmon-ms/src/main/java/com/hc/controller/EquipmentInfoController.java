package com.hc.controller;

import com.hc.application.EquipmentInfoApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/equipmentInfo")
public class EquipmentInfoController {

    @Autowired
    private EquipmentInfoApplication equipmentInfoApplication;


//    @GetMapping("/getEquipmentCurrentData")
//    @ApiOperation("查询所有设备当前值信息")
//    public List<Monitorequipment> getEquipmentCurrentData(@RequestParam("hospitalCode")String hospitalCode,
//                                                          @RequestParam("equipmentTypeId")String equipmentTypeId){
//        return equipmentInfoApplication.findEquipmentCurrentData(hospitalCode,equipmentTypeId);
//    }

}
