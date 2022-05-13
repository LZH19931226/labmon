package com.hc.application;


import com.hc.config.RedisUtils;
import com.hc.my.common.core.redis.namespace.TcpServiceEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TcpClientReidsSyncApplication {

    @Autowired
    private RedisUtils redisUtils;


    public void addDeviceChannel(String sn, String channelId) {
        redisUtils.hset(TcpServiceEnum.TCPCLIENT.getCode(),sn, channelId);
    }

    public void addChannelDevice(String Sn, String channelId) {
        redisUtils.hset(TcpServiceEnum.SNCLIENT.getCode(),channelId, Sn);
    }

    public void deleteDeviceChannel(String Sn, String channelId) {
        redisUtils.hdel(TcpServiceEnum.TCPCLIENT.getCode(),Sn);
        redisUtils.hdel(TcpServiceEnum.SNCLIENT.getCode(),channelId);
    }

    public String getSnBychannelId(String channelId) {
        Object o = redisUtils.hget(TcpServiceEnum.SNCLIENT.getCode(),channelId);
        if (null==o){
            return null;
        }
        return (String) o;
    }
}
