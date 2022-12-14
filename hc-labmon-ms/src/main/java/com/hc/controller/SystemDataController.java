package com.hc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.SystemDataApplication;
import com.hc.application.command.EquipmentDataCommand;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
