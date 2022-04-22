package com.hc.controller;

import com.hc.application.MonitorEquipmentApplication;
import com.hc.application.command.MonitorEquipmentCommand;
import com.hc.vo.equimenttype.MonitorEquipmentVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @author hc
 */
@RestController
@RequestMapping("/equipmentInfo")
public class MonitorEquipmentController {

    @Autowired
    private MonitorEquipmentApplication monitorEquipmentApplication;

    @PostMapping("/findEquipmentInfo")
    @ApiOperation("分页查询设备信息")
    public List<MonitorEquipmentVo> getEquipmentInfo(@RequestBody MonitorEquipmentCommand monitorEquipmentCommand){
        return monitorEquipmentApplication.getEquipmentInfoList(monitorEquipmentCommand);
    }
}
