package com.hc.controller;

import com.hc.application.EquipmentInfoApplication;
import com.hc.command.labmanagement.model.HospitalMadel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/equipmentInfo")
public class EquipmentInfoController {

    @Autowired
    private EquipmentInfoApplication equipmentInfoApplication;

    /**
     * 查询医院信息
     * @param hospitalCode
     * @return
     */
    @GetMapping("/findHospitalInfo")
    @ApiOperation("查询医院信息")
    public HospitalMadel getHospitalInfO(@RequestParam("hospitalCode")String hospitalCode){
        return equipmentInfoApplication.getHospitalInfO(hospitalCode);
    }
}
