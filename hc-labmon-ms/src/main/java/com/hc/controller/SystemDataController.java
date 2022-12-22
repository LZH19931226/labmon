package com.hc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.SystemDataApplication;
import com.hc.application.command.EquipmentDataCommand;
import com.hc.application.response.SummaryOfAlarmsResult;
import com.hc.my.common.core.jwt.JwtIgnore;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

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

}
