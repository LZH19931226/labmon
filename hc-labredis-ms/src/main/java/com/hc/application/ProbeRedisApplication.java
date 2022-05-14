package com.hc.application;

import com.hc.config.RedisUtils;
import com.hc.my.common.core.redis.dto.InstrumentInfoDto;
import com.hc.my.common.core.util.BeanConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class ProbeRedisApplication {

    @Autowired
    private RedisUtils redisUtils;

    public InstrumentInfoDto getProbeRedisInfo(String hospitalCode, String instrumentNo) {
        Object instrumentInfoDto = redisUtils.hget("P" + hospitalCode, instrumentNo);
        if(ObjectUtils.isEmpty(instrumentInfoDto)){
            return null;
        }
        return BeanConverter.convert(instrumentInfoDto,InstrumentInfoDto.class);
    }
}
