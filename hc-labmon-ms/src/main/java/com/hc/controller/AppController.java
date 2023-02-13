package com.hc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.AppEquipmentInfoApplication;
import com.hc.application.command.AlarmSystemCommand;
import com.hc.application.command.CurveCommand;
import com.hc.application.command.ProbeCommand;
import com.hc.application.command.WarningCommand;
import com.hc.application.curvemodel.CurveDataModel;
import com.hc.application.response.*;
import com.hc.clickhouse.po.Warningrecord;
import com.hc.dto.*;
import com.hc.my.common.core.jwt.JwtIgnore;
import com.hc.my.common.core.util.date.DateDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app")
public class AppController {

    @Autowired
    private AppEquipmentInfoApplication equipmentInfoAppApplication;

    /*首页*/
    @GetMapping("/getEquipmentNum")
    @ApiOperation("获取首页设备数量信息")
    public List<HospitalEquipmentDto> getEquipmentNum(@RequestParam("hospitalCode")String hospitalCode,@RequestParam("tags")String  tags){
        return equipmentInfoAppApplication.getEquipmentNum(hospitalCode,tags);
    }

    /*设备当前值*/
    @PostMapping("/getCurrentProbeInfo")
    @ApiOperation("获取探头当前值(卡片)")
    public CurrentProbeInfoResult getTheCurrentValueOfTheProbe(@RequestBody ProbeCommand probeCommand){
        return  equipmentInfoAppApplication.getTheCurrentValueOfTheProbe(probeCommand);
    }

    /**
     * 获取设备ups值
     * */
    @GetMapping("/getCurrentUps")
    @ApiOperation("获取设备ups信息")
    public List<MonitorUpsInfoDto> getCurrentUps(@RequestParam("hospitalCode")String hospitalCode,@RequestParam("equipmentTypeId")String equipmentTypeId){
        return equipmentInfoAppApplication.getCurrentUps(hospitalCode,equipmentTypeId);
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

    @PostMapping("/getEquipmentCurveInfo")
    @ApiOperation("查询app设备曲线值")
    public List<Map<String, CurveDataModel>> getCurveInfo(@RequestBody CurveCommand curveCommand){
        return equipmentInfoAppApplication.getCurveFirst(curveCommand);
    }

    /*报警信息*/
    @PostMapping("/getWarningInfo")
    @ApiOperation("获取设备报警信息")
    public List<WarningRecordInfo> getWarningInfo(@RequestBody WarningCommand warningCommand){
        return equipmentInfoAppApplication.getWarningInfoList(warningCommand);
    }

    @PostMapping("/getWarningDetailInfo")
    @ApiOperation("获取设备报警详情信息")
    public Page getWarningDetailInfo(@RequestBody WarningCommand warningCommand){
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

    @JwtIgnore
    @GetMapping("/synchronizedDeviceAlarmSwitch")
    @ApiOperation("同步设备报警开关")
    public void synchronizedDeviceAlarmSwitch(){
        equipmentInfoAppApplication.synchronizedDeviceAlarmSwitch();
    }

    @PostMapping("/getTheNumberOfAlarmSettingDevices")
    @ApiOperation("获取报警设置设备数量")
    public AlarmHand getTheNumberOfAlarmSettingDevices(@RequestBody AlarmSystemCommand alarmSystemCommand){
        return equipmentInfoAppApplication.getTheNumberOfAlarmSettingDevices(alarmSystemCommand);
    }

}
