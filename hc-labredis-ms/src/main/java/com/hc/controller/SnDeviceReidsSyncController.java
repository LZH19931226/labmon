package com.hc.controller;

import com.hc.application.SnDeviceReidsSyncApplocation;
import com.hc.my.common.core.jwt.JwtIgnore;
import com.hc.my.common.core.redis.command.EquipmentInfoCommand;
import com.hc.my.common.core.redis.dto.MonitorequipmentlastdataDto;
import com.hc.my.common.core.redis.dto.SnDeviceDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/snDevice")
public class SnDeviceReidsSyncController {

    @Autowired
    private SnDeviceReidsSyncApplocation snDeviceReidsSyncApplocation;

    /**
     * 新增,修改sn设备信息
     * @param snDeviceDto sn设备缓存对象
     */
    @PostMapping("/updateSnDeviceDtoSync")
    @ApiOperation("新增,修改sn设备信息")
    public void updateSnDeviceDtoSync(@RequestBody SnDeviceDto snDeviceDto){
        snDeviceReidsSyncApplocation.updateSnDeviceDtoSync(snDeviceDto);
    }

    /**
     * 获取设备缓存信息
     * @param sn sn
     * @return sn涉笔缓存对象
     */
    @GetMapping("/getSnDeviceDto")
    @ApiOperation("获取设备缓存信息")
    public SnDeviceDto getSnDeviceDto(@RequestParam("sn") String sn){
        return snDeviceReidsSyncApplocation.getSnDeviceDto(sn);
    }

    /**
     * 删除设备缓存信息
     * @param sn sn
     */
    @GetMapping("/deleteSnDeviceDto")
    @ApiOperation("删除设备缓存信息")
    public void deleteSnDeviceDto(@RequestParam("sn")String sn){
        snDeviceReidsSyncApplocation.deleteSnDeviceDto(sn);
    }

    /**
     * 新增更新设备当前值信息
     * @param monitorequipmentlastdataDto 最新数据
     */
    @PostMapping("/updateSnCurrentInfo")
    @ApiOperation("新增更新设备当前值信息")
    public void  updateSnCurrentInfo(@RequestBody MonitorequipmentlastdataDto monitorequipmentlastdataDto){
        snDeviceReidsSyncApplocation.updateSnCurrentInfo(monitorequipmentlastdataDto);
    }

    /**
     * 获取设备当前值
     * @param hospitalCode 医院id
     * @param equipmentNo 设备id
     * @return
     */
    @GetMapping("/getCurrentDataInfo")
    @ApiOperation("获取设备当前值")
    public List<MonitorequipmentlastdataDto> getCurrentDataInfo(@RequestParam("hospitalCode")String hospitalCode,@RequestParam("equipmentNo")String equipmentNo){
        return snDeviceReidsSyncApplocation.getCurrentInfo(hospitalCode,equipmentNo);
    }

    /**
     * 删除设备当前值
     * @param hospitalCode 医院id
     * @param equipmentNo 设备id
     */
    @DeleteMapping("/deleteCurrentDataInfo")
    @ApiOperation("删除设备当前值")
    public void deleteCurrentInfo(@RequestParam("hospitalCode")String hospitalCode,@RequestParam("equipmentNo")String equipmentNo){
        snDeviceReidsSyncApplocation.remove(hospitalCode,equipmentNo);
    }

    /**
     * 批量获取设备当前值
     * @param equipmentInfoCommand 设备参数
     * @return
     */
    @PostMapping("/getTheCurrentValueOfTheDeviceInBatches")
    @ApiOperation("批量获取设备当前值")
    public  List<MonitorequipmentlastdataDto> getTheCurrentValueOfTheDeviceInBatches(@RequestBody EquipmentInfoCommand equipmentInfoCommand){
        return snDeviceReidsSyncApplocation.getTheCurrentValue(equipmentInfoCommand);
    }

    /**
     * 同步监控设备信息
     */
    @JwtIgnore
    @GetMapping("/monitorEquipmentInfoCache")
    @ApiOperation("同步监控设备信息")
    public void MonitorEquipmentInfoCache(){
        snDeviceReidsSyncApplocation.MonitorEquipmentInfoCache();
    }


    @GetMapping("/getLastDataListSize")
    @ApiOperation("获取批量缓存设备数据缓存数组长度")
    public Long getLastDataListSize(@RequestParam("listCode")String listCode){
        return snDeviceReidsSyncApplocation.getLastDataListSize(listCode);
    }

    @GetMapping("/getLeftPopLastData")
    @ApiOperation("从当前值数组缓存获取数据并移除")
    public MonitorequipmentlastdataDto getLeftPopLastData(@RequestParam("listCode")String listCode){
        return  snDeviceReidsSyncApplocation.getLeftPopLastData(listCode);
    }


}
