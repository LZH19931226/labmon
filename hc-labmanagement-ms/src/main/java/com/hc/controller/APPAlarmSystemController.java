package com.hc.controller;

import com.hc.application.MonitorEquipmentApplication;
import com.hc.application.command.AlarmSystemCommand;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
public class APPAlarmSystemController {

    @Autowired
    private MonitorEquipmentApplication monitorEquipmentApplication;

    @PostMapping("/updateProbeAlarmState")
    @ApiOperation("修改探头报警开关")
    public void updateProbeAlarmState(@RequestBody AlarmSystemCommand alarmSystemCommand){
        monitorEquipmentApplication.updateProbeAlarmState(alarmSystemCommand);
    }

    @PostMapping("/batchUpdateProbeAlarmState")
    @ApiOperation("修改设备报警开关")
    public void batchUpdateProbeAlarmState(@RequestBody AlarmSystemCommand alarmSystemCommand){
        monitorEquipmentApplication.batchUpdateProbeAlarmState(alarmSystemCommand);
    }

    @PostMapping("/batchOperationType")
    @ApiOperation(value = "批量禁用与启用当前设备类型下所有设备报警探头")
    public void batchOperationType(@RequestBody AlarmSystemCommand alarmSystemCommand) {
        monitorEquipmentApplication.batchOperationType(alarmSystemCommand);
    }
}
