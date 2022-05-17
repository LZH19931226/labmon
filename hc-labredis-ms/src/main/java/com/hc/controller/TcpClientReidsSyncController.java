package com.hc.controller;

import com.hc.application.TcpClientReidsSyncApplication;
import com.hc.my.common.core.redis.dto.ParamaterModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tcpclient")
public class TcpClientReidsSyncController {

    @Autowired
    private TcpClientReidsSyncApplication tcpClientReidsSyncApplication;

    @PostMapping("/addDeviceChannel")
    @ApiOperation("新增设备通道维护关系")
    public void addDeviceChannel(@RequestBody ParamaterModel paramaterModel) {
        tcpClientReidsSyncApplication.addDeviceChannel(paramaterModel);
    }

    @GetMapping("/deleteDeviceChannel")
    @ApiOperation("删除通道设备维护关系")
    public void deleteDeviceChannel(@RequestParam("sn") String sn, @RequestParam("cmdId") String cmdId){
        tcpClientReidsSyncApplication.deleteDeviceChannel(sn,cmdId);
    }

    @GetMapping("/getSnBychannelId")
    @ApiOperation("通道获取sn号信息")
    public ParamaterModel getSnBychannelId(@RequestParam("sn") String sn,@RequestParam("cmdId") String cmdId){
        return tcpClientReidsSyncApplication.getSnBychannelId(sn+cmdId);
    }



}
