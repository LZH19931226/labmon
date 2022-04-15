package com.hc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.appliction.HospitalInfoApplication;
import com.hc.appliction.command.HospitalCommand;
import com.hc.vo.hospital.HospitalInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 医院信息控制层
 * @author hc
 */
@RestController
@RequestMapping("/hospitalInfo")
@Api(tags = "医院管理")
public class HospitalInfoController {

    @Autowired
    private HospitalInfoApplication hospitalInfoApplication;

    @PostMapping("/list")
    @ApiOperation(value = "查询医院信息")
    public Page<HospitalInfoVo> list(@RequestBody  HospitalCommand hospitalCommand){
        return hospitalInfoApplication.selectHospitalInfo(hospitalCommand, hospitalCommand.getPageSize(), hospitalCommand.getPageCurrent());
    }

    @PostMapping("/addHospitalInfo")
    @ApiOperation(value = "添加医院")
    public void add(@ApiParam(name = "hospitalCommand" , value = "医院信息" , required = true)
                                 @RequestBody  HospitalCommand hospitalCommand){
        hospitalInfoApplication.insertHospitalInfo(hospitalCommand);
    }

    @PutMapping("/editHospitalInfo")
    public void edit(@ApiParam(name = "BsIncubatorCommand", value = "培养箱信息模型", required = true)
                     @RequestBody HospitalCommand hospitalCommand){
        hospitalInfoApplication.editHospitalInfo(hospitalCommand);
    }
}
