package com.hc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.MonitorEquipmentApplication;
import com.hc.application.command.MonitorEquipmentCommand;
import com.hc.application.command.WiredEqCommand;
import com.hc.my.common.core.jwt.JwtIgnore;
import com.hc.my.common.core.redis.dto.SnDeviceDto;
import com.hc.vo.equimenttype.InstrumentmonitorVo;
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

    @GetMapping("/findHardwareTypeProbeInformation")
    @ApiOperation("获取硬件设备类型对应监控探头信息")
    private List<MonitorinstrumenttypeVo> getHardwareTypeProbeInformation(@RequestParam("equipmentTypeId") String equipmentTypeId){
        return monitorEquipmentApplication.getHardwareTypeProbeInformation(equipmentTypeId);
    }

    /**
     *获取设备配置信息
     * */
    @GetMapping("/getInstrumentMonitorInfo")
    @ApiOperation("获取设备配置信息")
    private List<InstrumentmonitorVo> getProbeInfoByITypeId(@RequestParam("instrumentTypeId") String instrumentTypeId){
        return monitorEquipmentApplication.getProbeInfoByITypeId(instrumentTypeId);
    }

    @PostMapping("/findEquipmentInfo")
    @ApiOperation("分页查询设备信息")
    public Page<MonitorEquipmentVo> getEquipmentInfo(@RequestBody MonitorEquipmentCommand monitorEquipmentCommand){
        return monitorEquipmentApplication.getEquipmentInfo(monitorEquipmentCommand);
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

    @DeleteMapping("/{equipmentNo}")
    @ApiOperation("删除设备")
    public void deleteMonitorEquipment(@PathVariable("equipmentNo") String equipmentNo){
        monitorEquipmentApplication.deleteMonitorEquipment(equipmentNo);
    }

    @GetMapping("/selectMonitorEquipmentType")
    @ApiOperation("获取设备所有监控类型")
    public  List<MonitorinstrumenttypeVo>  selectMonitorEquipmentType(@RequestParam(value = "instrumenttypeid",required = false) String instrumenttypeid){
         return monitorEquipmentApplication.selectMonitorEquipmentType(instrumenttypeid);
    }

    @JwtIgnore
    @GetMapping("/getAllMonitorEquipmentInfo")
    @ApiOperation("获取所有的监控设备信息")
    public List<SnDeviceDto> getAllMonitorEquipmentInfo(){
        return monitorEquipmentApplication.getAllMonitorEquipmentInfo();
    }

    @JwtIgnore
    @GetMapping("/getHospitalUPS")
    @ApiOperation("获取医院ups的设备no集合")
    public List<SnDeviceDto> getEquipmentNoList(@RequestParam("hospitalCode")String hospitalCode,@RequestParam("equipmentTypeId")String equipmentTypeId){
        return monitorEquipmentApplication.getEquipmentNoList(hospitalCode,equipmentTypeId);
    }

    @JwtIgnore
    @GetMapping("/getMonitorEquipmentInfoByEno")
    @ApiOperation("获取监控设备查询信息")
    public SnDeviceDto selectMonitorEquipmentInfoByEno(@RequestParam("equipmentNo")String equipmentNo){
        return monitorEquipmentApplication.selectMonitorEquipmentInfoByEno(equipmentNo);
    }

    @JwtIgnore
    @GetMapping("/getMonitorEquipmentInfoByHCode")
    @ApiOperation("获取医院的设备信息")
    public List<SnDeviceDto> getMonitorEquipmentInfoByHCode(@RequestParam("hospitalCode") String hospitalCode){
        return monitorEquipmentApplication.getMonitorEquipmentInfoByHCode(hospitalCode);
    }

    @GetMapping("/checkSn")
    @ApiOperation("判断sn是否已存在")
    public Boolean checkSn(@RequestParam(value = "sn")String sn){
        return monitorEquipmentApplication.checkSn(sn);
    }

    @GetMapping("/getHosEqTypeEqInfo")
    @ApiOperation("/获取医院设备类型设备位置信息")
    public List<String> getHosEqTypeEqInfo(@RequestParam("hospitalCode") String hospitalCode,@RequestParam("equipmentTypeId")String equipmentTypeId){
        return  monitorEquipmentApplication.getHosEqTypeEqInfo(hospitalCode,equipmentTypeId);
    }

    @PostMapping("/addWiredEquipment")
    @ApiOperation("/新增有线设备")
    public void addWiredEquipment(@RequestBody WiredEqCommand wiredEqCommand){
        monitorEquipmentApplication.addWiredEquipment(wiredEqCommand);
    }


    @PostMapping("/updateEquipmentEnableSet")
    @ApiOperation("编辑设置设备")
    public void updateEquipmentEnableSet(@RequestBody MonitorEquipmentCommand monitorEquipmentCommand){
        monitorEquipmentApplication.updateEquipmentEnableSet(monitorEquipmentCommand);
    }

    @PostMapping("/updateEquipmentIns")
    @ApiOperation("编辑设备的监测类型")
    public void  updateEquipmentIns(@RequestBody MonitorEquipmentCommand monitorEquipmentCommand){
        monitorEquipmentApplication.updateEquipmentIns(monitorEquipmentCommand);
    }
}
