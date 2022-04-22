package com.hc.controller;

import com.hc.application.MonitorEquipmentApplication;
import com.hc.application.command.MonitorEquipmentCommand;
import com.hc.vo.equimenttype.MonitorEquipmentVo;
import com.hc.vo.equimenttype.MonitorinstrumenttypeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * @author hc
 */
@RestController
@Api(tags = "设备管理")
@RequestMapping("/equipmentInfo")
public class MonitorEquipmentController {

    @Autowired
    private MonitorEquipmentApplication monitorEquipmentApplication;

    @PostMapping("/findEquipmentInfo")
    @ApiOperation("分页查询设备信息")
    public List<MonitorEquipmentVo> getEquipmentInfo(@RequestBody MonitorEquipmentCommand monitorEquipmentCommand){
        return monitorEquipmentApplication.getEquipmentInfoList(monitorEquipmentCommand);
    }

    @PostMapping("/addMonitorEquipment")
    @ApiOperation("新增设备")
    public void  addMonitorEquipment(@RequestBody MonitorEquipmentCommand monitorEquipmentCommand){
        monitorEquipmentApplication.addMonitorEquipment(monitorEquipmentCommand);
    }

    @PostMapping("/updateMonitorEquipment")
    @ApiOperation("编辑设备")
    public void updateMonitorEquipment(@RequestBody MonitorEquipmentCommand monitorEquipmentCommand){
        monitorEquipmentApplication.updateMonitorEquipment(monitorEquipmentCommand);
    }

    @DeleteMapping("/{equipmentId}")
    @ApiOperation("删除设备")
    public void deleteMonitorEquipment(@PathVariable("equipmentId") String equipmentId){
        monitorEquipmentApplication.deleteMonitorEquipment(equipmentId);
    }

    @GetMapping("/selectMonitorEquipmentType")
    @ApiOperation("获取设备所有监控类型")
    public  List<MonitorinstrumenttypeVo>  selectMonitorEquipmentType(@RequestParam(value = "instrumenttypeid",required = false) String instrumenttypeid){
         return monitorEquipmentApplication.selectMonitorEquipmentType(instrumenttypeid);
    }
}
