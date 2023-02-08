package com.hc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.SystemDataApplication;
import com.hc.application.command.EquipmentDataCommand;
import com.hc.application.response.InstrumentTypeNumResult;
import com.hc.application.response.SummaryOfAlarmsResult;
import com.hc.clickhouse.po.Warningrecord;
import com.hc.dto.AlarmEquipmentNumDto;
import com.hc.dto.EquipmentTypeNumDto;
import com.hc.dto.InstrumentTypeNumDto;
import com.hc.dto.eqTypeAlarmNumCountDto;
import com.hc.my.common.core.jwt.JwtIgnore;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(value = "/system")
public class SystemDataController {

    @Autowired
    private SystemDataApplication systemDataApplication;

    @PostMapping("/findPacketLossLog")
    @ApiOperation("获取设备心跳应答详情")
    public Page findPacketLossLog(@RequestBody EquipmentDataCommand equipmentDataCommand) {
        return systemDataApplication.findPacketLossLog(equipmentDataCommand);
    }

    @PostMapping("/getPacketLossColumnar")
    @ApiOperation("获取柱状图心跳丢包率")
    public SummaryOfAlarmsResult getPacketLossColumnar(@RequestBody EquipmentDataCommand equipmentDataCommand){
        return systemDataApplication.getPacketLossColumnar(equipmentDataCommand);
    }

    @GetMapping("/exportPacketLossLog")
    @ApiOperation("excel导出心跳详情数据")
    @JwtIgnore
    public void exportPacketLossLog(EquipmentDataCommand equipmentDataCommand, HttpServletResponse response){
        systemDataApplication.exportPacketLossLog(equipmentDataCommand,response);
    }

    @PostMapping("/eqTypeAlarmNumCount")
    @ApiOperation("设备类型报警数量统计")
    public List<eqTypeAlarmNumCountDto> eqTypeAlarmNumCount(@RequestBody EquipmentDataCommand equipmentDataCommand){
      return systemDataApplication.eqTypeAlarmNumCount(equipmentDataCommand);
    }

    @GetMapping("/getWarningRecordInfo")
    @ApiOperation("近期报警数据")
    public List<Warningrecord> getWarningRecordInfo(@RequestParam("hospitalCode")String hospitalCode, @RequestParam("count")Integer count){
        return systemDataApplication.getWarningRecordInfo(hospitalCode,count);
    }

    @PostMapping("/getEqAlarmPeriod")
    @ApiOperation("设备报警时段分布")
    public List<eqTypeAlarmNumCountDto> getEqAlarmPeriod(@RequestBody EquipmentDataCommand equipmentDataCommand){
        return systemDataApplication.getEqAlarmPeriod(equipmentDataCommand);
    }
    @PostMapping("getEquipmentNumProportion")
    @ApiOperation("获取不同类型设备数量")
    public List<EquipmentTypeNumDto> getEquipmentNumProportion(@RequestBody EquipmentDataCommand equipmentDataCommand){
        return systemDataApplication.getEquipmentNumProportion(equipmentDataCommand);
    }

    @PostMapping("/getInstrumentNum")
    @ApiOperation("获取不同设备数量")
    public InstrumentTypeNumResult getInstrumentNum(@RequestBody EquipmentDataCommand equipmentDataCommand){
        return systemDataApplication.getInstrumentNum(equipmentDataCommand);
    }

    @PostMapping("/getAlarmDeviceNum")
    @ApiOperation("获取高频设备数量")
    public List<AlarmEquipmentNumDto> getAlarmDeviceNum(@RequestBody EquipmentDataCommand equipmentDataCommand){
        return systemDataApplication.getAlarmDeviceNum(equipmentDataCommand);
    }
}
