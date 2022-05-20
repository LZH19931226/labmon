package com.hc.controller;

import com.hc.application.SnDeviceReidsSyncApplocation;
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

    @PostMapping("/updateSnDeviceDtoSync")
    @ApiOperation("新增,修改sn设备信息")
    public void updateSnDeviceDtoSync(@RequestBody SnDeviceDto snDeviceDto){
        snDeviceReidsSyncApplocation.updateSnDeviceDtoSync(snDeviceDto);
    }

    @GetMapping("/getSnDeviceDto")
    @ApiOperation("获取设备缓存信息")
    public SnDeviceDto getSnDeviceDto(@RequestParam("sn") String sn){
        return snDeviceReidsSyncApplocation.getSnDeviceDto(sn);
    }
    @GetMapping("/deleteSnDeviceDto")
    @ApiOperation("删除设备缓存信息")
    public void deleteSnDeviceDto(@RequestParam("sn")String sn){
        snDeviceReidsSyncApplocation.deleteSnDeviceDto(sn);
    }

    @PostMapping("/updateSnCurrentInfo")
    @ApiOperation("新增更新设备当前值信息")
    public void  updateSnCurrentInfo(@RequestBody MonitorequipmentlastdataDto monitorequipmentlastdataDto){
        snDeviceReidsSyncApplocation.updateSnCurrentInfo(monitorequipmentlastdataDto);
    }

    @GetMapping("/getCurrentDataInfo")
    @ApiOperation("获取设备当前值")
    public List<MonitorequipmentlastdataDto> getCurrentDataInfo(@RequestParam("hospitalCode")String hospitalCode,@RequestParam("equipmentNo")String equipmentNo){
        return snDeviceReidsSyncApplocation.getCurrentInfo(hospitalCode,equipmentNo);
    }

    @DeleteMapping("/deleteCurrentDataInfo")
    @ApiOperation("删除设备当前值")
    public void deleteCurrentInfo(@RequestParam("hospitalCode")String hospitalCode,@RequestParam("equipmentNo")String equipmentNo){
        snDeviceReidsSyncApplocation.remove(hospitalCode,equipmentNo);
    }

    @GetMapping("/getTheCurrentValueOfTheDeviceInBatches")
    @ApiOperation("批量获取设备当前值")
    public  List<MonitorequipmentlastdataDto> getTheCurrentValueOfTheDeviceInBatches(@RequestParam("hospitalCode")String hospitalCode,
                                                                                     @RequestParam("equipmentNoList")List<String> equipmentNoList){
        return snDeviceReidsSyncApplocation.getTheCurrentValue(hospitalCode,equipmentNoList);
    }

}
