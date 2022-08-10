package com.hc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.EquipmentInfoAppApplication;
import com.hc.application.command.CurveCommand;
import com.hc.application.command.ProbeCommand;
import com.hc.application.command.WarningCommand;
import com.hc.application.response.WarningRecordInfo;
import com.hc.clickhouse.po.Warningrecord;
import com.hc.dto.*;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/app")
public class AppController {

    @Autowired
    private EquipmentInfoAppApplication equipmentInfoAppApplication;

    /*首页*/
    @GetMapping("/getEquipmentNum")
    @ApiOperation("获取首页设备数量信息")
    public List<HospitalEquipmentDto> getEquipmentNum(@Param("hospitalCode")String hospitalCode){
        return equipmentInfoAppApplication.getEquipmentNum(hospitalCode);
    }

    /*设备当前值*/
    @PostMapping("/getCurrentProbeInfo")
    @ApiOperation("获取探头当前值")
    public Page<ProbeCurrentInfoDto> getTheCurrentValueOfTheProbe(@RequestBody ProbeCommand probeCommand){
        return  equipmentInfoAppApplication.getTheCurrentValueOfTheProbe(probeCommand);
    }

    /*设备详情*/
    @GetMapping("/getEquipmentRunningTime")
    @ApiOperation("获取设备运行时间")
    public DateDto getEquipmentRunningTime(@RequestParam("equipmentNo")String equipmentNo){
        return equipmentInfoAppApplication.getEquipmentRunningTime(equipmentNo);
    }

    @GetMapping("/getImplementerInformation")
    @ApiOperation("获取实施人员信息")
    public List<UserRightDto> getImplementerInformation(@RequestParam("hospitalCode")String hospitalCode){
        return equipmentInfoAppApplication.getImplementerInformation(hospitalCode);
    }

    @GetMapping("/getNumUnreadDeviceAlarms")
    @ApiOperation("获取设备报警未读数量")
    public List<Warningrecord> getNumUnreadDeviceAlarms(@RequestParam("equipmentNo")String equipmentNo){
        return equipmentInfoAppApplication.getNumUnreadDeviceAlarms(equipmentNo);
    }

    @PostMapping("/getEuipmentCurveInfo")
    @ApiOperation("查询app设备曲线值")
    public CurveInfoDto getCurveInfo(@RequestBody CurveCommand curveCommand){
        return equipmentInfoAppApplication.getCurveFirst(curveCommand);
    }

    /*报警信息*/
    @PostMapping("/getWarningInfo")
    @ApiOperation("获取报警信息")
    public List<WarningRecordInfo> getWarningInfo(@RequestBody @Valid WarningCommand warningCommand){
        return equipmentInfoAppApplication.getWarningInfo(warningCommand);
    }
}
