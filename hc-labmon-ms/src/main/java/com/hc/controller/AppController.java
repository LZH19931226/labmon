package com.hc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.EquipmentInfoAppApplication;
import com.hc.application.command.AlarmSystemCommand;
import com.hc.application.command.CurveCommand;
import com.hc.application.command.ProbeCommand;
import com.hc.application.command.WarningCommand;
import com.hc.application.response.WarningDetailInfo;
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
    @ApiOperation("获取设备报警信息")
    public List<WarningRecordInfo> getWarningInfo(@RequestBody WarningCommand warningCommand){
        return equipmentInfoAppApplication.getWarningInfo(warningCommand);
    }

    @PostMapping("getWarningDetailInfo")
    @ApiOperation("获取设备报警详情信息")
    public List<WarningDetailInfo> getWarningDetailInfo(@RequestBody WarningCommand warningCommand){
        return equipmentInfoAppApplication.getWarningDetailInfo(warningCommand);
    }

    /***
     * 1.查询  ename sn
     * 2.单个修改
     * 3.批量修改
     */
    /*设备报警设置*/
    @PostMapping("/getAlarmSystemInfo")
    @ApiOperation("分页获取设备报警设置信息")
    public Page getAlarmSystemInfo(@RequestBody ProbeCommand probeCommand){
        return equipmentInfoAppApplication.getAlarmSystemInfo(probeCommand);
    }

    @PostMapping("/updateProbeAlarmState")
    @ApiOperation("修改探头报警开关")
    public void updateProbeAlarmState(@RequestBody AlarmSystemCommand alarmSystemCommand){
        equipmentInfoAppApplication.updateProbeAlarmState(alarmSystemCommand);
    }

    @PostMapping("/batchUpdateProbeAlarmState")
    @ApiOperation("修改设备报警开关")
    public void batchUpdateProbeAlarmState(@RequestBody AlarmSystemCommand alarmSystemCommand){
        equipmentInfoAppApplication.batchUpdateProbeAlarmState(alarmSystemCommand);
    }

    @GetMapping("/synchronizedDeviceAlarmSwitch")
    @ApiOperation("同步设备报警开关")
    public void synchronizedDeviceAlarmSwitch(){
        equipmentInfoAppApplication.synchronizedDeviceAlarmSwitch();
    }

    @PostMapping("/batchOperationType")
    @ApiOperation(value = "批量禁用与启用当前设备类型下所有设备报警探头")
    public void batchOperationType(@RequestBody AlarmSystemCommand alarmSystemCommand) {
        equipmentInfoAppApplication.batchOperationType(alarmSystemCommand);
    }

}
