package com.hc.application;

import cn.hutool.json.JSONUtil;
import com.hc.config.RedisUtils;
import com.hc.my.common.core.redis.dto.SnDeviceDto;
import com.hc.my.common.core.redis.namespace.LabManageMentServiceEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class SnDeviceReidsSyncApplocation {

    @Autowired
    private RedisUtils redisUtils;

    public void updateSnDeviceDtoSync(SnDeviceDto snDeviceDto) {
        redisUtils.hset(LabManageMentServiceEnum.DEVICEINFO.getCode(),snDeviceDto.getSn(), JSONUtil.toJsonStr(snDeviceDto));
    }

    public SnDeviceDto getSnDeviceDto(String sn) {
        Object snInfo = redisUtils.hget(LabManageMentServiceEnum.DEVICEINFO.getCode(),sn);
        if (null==snInfo){
            return null;
        }
        return JSONUtil.toBean((String) snInfo,SnDeviceDto.class);
    }

    public void deleteSnDeviceDto(String sn) {
        redisUtils.hdel(LabManageMentServiceEnum.DEVICEINFO.getCode(),sn);
    }
}
