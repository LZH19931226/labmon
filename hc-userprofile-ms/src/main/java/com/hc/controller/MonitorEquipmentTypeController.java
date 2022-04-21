package com.hc.controller;

import com.hc.appliction.MonitorEquipmentTypeApplication;
import com.hc.vo.equimenttype.MonitorEquipmentTypeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author hc
 */
@RestController
@RequestMapping(value = "/monitorEquipmentType")
@Api(tags = "监控设备类型")
public class MonitorEquipmentTypeController {

    @Autowired
    private MonitorEquipmentTypeApplication monitorEquipmentTypeApplication;

    @GetMapping("/findMonitorEquipmentTypeList")
    @ApiOperation("获取监控设备类型列表")
    public List<MonitorEquipmentTypeVo> getMonitorEquipmentTypeList(){
        return monitorEquipmentTypeApplication.getMonitorEquipmentTypeList();
    }
}
