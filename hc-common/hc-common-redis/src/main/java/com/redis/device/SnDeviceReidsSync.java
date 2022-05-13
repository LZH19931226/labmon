package com.redis.device;


import com.hc.my.common.core.redis.dto.SnDeviceDto;

public interface SnDeviceReidsSync {
    //新增,修改
    void updateSnDeviceDtoSync(SnDeviceDto snDeviceDto);
    //获取设备缓存信息
    SnDeviceDto getSnDeviceDto(String sn);
    //删除设备缓存信息
    void deleteSnDeviceDto(String sn);
    //新增设备通道维护关系
    void addDeviceChannel(String Sn,String channelId);
    //新增通道设备维护关系
    void addChannelDevice(String Sn,String channelId);
    //删除通道设备维护关系
    void deleteDeviceChannel(String Sn,String channelId);
    //通道获取sn号信息
    String getSnBychannelId(String channelId);
}
