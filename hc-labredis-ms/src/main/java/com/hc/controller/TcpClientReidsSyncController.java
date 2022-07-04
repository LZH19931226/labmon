package com.hc.controller;

import com.hc.application.TcpClientReidsSyncApplication;
import com.hc.my.common.core.redis.dto.ParamaterModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @GetMapping("/saveChannelIdSn")
    @ApiOperation("保存通道sn号信息")
    public void saveChannelIdSn(@RequestParam("sn") String sn,@RequestParam("channelId") String channelId){
         tcpClientReidsSyncApplication.saveChannelIdSn(sn,channelId);
    }

    @GetMapping("/deleteChannelIdSn")
    @ApiOperation("删除通道sn关联信息")
    public void deleteChannelIdSn(@RequestParam("channelId") String channelId){
        tcpClientReidsSyncApplication.deleteChannelIdSn(channelId);
    }

    @GetMapping("/getAllClientInfo")
    @ApiOperation("获取通道缓存最新sn信息")
    public Map<Object,Object>  getAllClientInfo(){
          return  tcpClientReidsSyncApplication.getAllClientInfo();
    }

    @GetMapping("/deleteHashKey")
    @ApiOperation("删除指定hashkey里面所有数据")
    public void deleteHashKey(@RequestParam("hashKey") String hashKey){
        tcpClientReidsSyncApplication.deleteHashKey(hashKey);
    }

}
