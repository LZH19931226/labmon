package com.hc.controller;

import com.hc.application.TcpClientReidsSyncApplication;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tcpclient")
public class TcpClientReidsSyncController {

    @Autowired
    private TcpClientReidsSyncApplication tcpClientReidsSyncApplication;

    @GetMapping("/addDeviceChannel")
    @ApiOperation("新增设备通道维护关系")
    public void addDeviceChannel(@RequestParam("sn") String sn, @RequestParam("channelId") String channelId) {
        tcpClientReidsSyncApplication.addDeviceChannel(sn, channelId);
    }

    @GetMapping("/addChannelDevice")
    @ApiOperation("新增通道设备维护关系")
    public void addChannelDevice(@RequestParam("sn") String sn, @RequestParam("channelId") String channelId){
        tcpClientReidsSyncApplication.addChannelDevice(sn,channelId);
    }

    @GetMapping("/deleteDeviceChannel")
    @ApiOperation("删除通道设备维护关系")
    public void deleteDeviceChannel(@RequestParam("sn") String sn, @RequestParam("channelId") String channelId){
        tcpClientReidsSyncApplication.deleteDeviceChannel(sn,channelId);
    }

    @GetMapping("/getSnBychannelId")
    @ApiOperation("通道获取sn号信息")
    public String getSnBychannelId(@RequestParam("channelId") String channelId){
        return tcpClientReidsSyncApplication.getSnBychannelId(channelId);
    }


}
