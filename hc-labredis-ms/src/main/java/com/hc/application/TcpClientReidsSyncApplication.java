package com.hc.application;


import cn.hutool.json.JSONUtil;
import com.hc.config.RedisUtils;
import com.hc.my.common.core.redis.dto.ParamaterModel;
import com.hc.my.common.core.redis.namespace.TcpServiceEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TcpClientReidsSyncApplication {

    @Autowired
    private RedisUtils redisUtils;


    public void addDeviceChannel(ParamaterModel paramaterModel) {
        redisUtils.hset(TcpServiceEnum.TCPCLIENT.getCode(),paramaterModel.getSN()+paramaterModel.getCmdid(), JSONUtil.toJsonStr(paramaterModel));
    }

    public void deleteDeviceChannel(String Sn, String cmdId) {
        redisUtils.hdel(TcpServiceEnum.TCPCLIENT.getCode(),Sn+cmdId);
    }


    public ParamaterModel getSnBychannelId(String snCmdId) {
        Object o = redisUtils.hget(TcpServiceEnum.TCPCLIENT.getCode(),snCmdId);
        if (null==o){
            return null;
        }
        return JSONUtil.toBean((String) o,ParamaterModel.class);
    }
}
