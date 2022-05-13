package com.redis.device.impl;

import cn.hutool.json.JSONUtil;
import com.hc.my.common.core.redis.dto.SnDeviceDto;
import com.hc.my.common.core.redis.namespace.LabManageMentServiceEnum;
import com.hc.my.common.core.redis.namespace.TcpServiceEnum;
import com.redis.device.SnDeviceReidsSync;
import com.redis.util.RedisTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SnDeviceReidsSyncImpl  implements SnDeviceReidsSync {

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

    @Override
    public void updateSnDeviceDtoSync(SnDeviceDto snDeviceDto) {
        redisTemplateUtil.boundHashOps(LabManageMentServiceEnum.DEVICEINFO.getCode()).put(snDeviceDto.getSn(), JSONUtil.toJsonStr(snDeviceDto));
    }

    @Override
    public SnDeviceDto getSnDeviceDto(String sn) {
        Object snInfo = redisTemplateUtil.boundHashOps(LabManageMentServiceEnum.DEVICEINFO.getCode()).get(sn);
        if (null==snInfo){
           return null;
        }
        return JSONUtil.toBean((String) snInfo,SnDeviceDto.class);
    }

    @Override
    public void deleteSnDeviceDto(String sn) {
        redisTemplateUtil.boundHashOps(LabManageMentServiceEnum.DEVICEINFO.getCode()).delete(sn);
    }

    @Override
    public void addDeviceChannel(String Sn, String channelId) {
        redisTemplateUtil.boundHashOps(TcpServiceEnum.TCPCLIENT.getCode()).put(Sn, channelId);
    }

    @Override
    public void addChannelDevice(String Sn, String channelId) {
        redisTemplateUtil.boundHashOps(TcpServiceEnum.SNCLIENT.getCode()).put(channelId, Sn);
    }

    @Override
    public void deleteDeviceChannel(String Sn, String channelId) {
        redisTemplateUtil.boundHashOps(TcpServiceEnum.TCPCLIENT.getCode()).delete(Sn);
        redisTemplateUtil.boundHashOps(TcpServiceEnum.SNCLIENT.getCode()).delete(channelId);
    }

    @Override
    public String getSnBychannelId(String channelId) {
        Object o = redisTemplateUtil.boundHashOps(TcpServiceEnum.SNCLIENT.getCode()).get(channelId);
        if (null==o){
           return null;
        }
        return (String) o;
    }
}
