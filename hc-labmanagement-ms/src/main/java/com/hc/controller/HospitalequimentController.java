package com.hc.controller;

import com.hc.application.HospitalequimentApplication;
import com.hc.application.command.HospitalEquimentTypeCommand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;




/**
 * 
 *
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
@RestController
@Api(tags = "医院设备类型管理")
@RequestMapping("/hospitalequimentType")
public class HospitalequimentController {

    @Autowired
    private HospitalequimentApplication hospitalequimentApplication;


    @PostMapping("/addHospitalEquimentType")
    @ApiOperation("新增医院设备类型")
    public void addHospitalEquimentType(@RequestBody HospitalEquimentTypeCommand hospitalEquimentTypeCommand){
        hospitalequimentApplication.addHospitalEquimentType(hospitalEquimentTypeCommand);
    }

    @PostMapping("updateHospitalEquimentType")
    @ApiOperation("编辑医院设备类型")
    public void updateHospitalEquimentType(@RequestBody HospitalEquimentTypeCommand hospitalEquimentTypeCommand){
        hospitalequimentApplication.updateHospitalEquimentType(hospitalEquimentTypeCommand);
    }

}
