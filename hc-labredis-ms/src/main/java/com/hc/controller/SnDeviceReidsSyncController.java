package com.hc.controller;

import com.hc.application.SnDeviceReidsSyncApplocation;
import com.hc.my.common.core.redis.dto.SnDeviceDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
