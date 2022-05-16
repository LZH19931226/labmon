package com.hc.application;

import cn.hutool.json.JSONUtil;
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

    /**
     * 获取医院探头缓存信息
     * @param hospitalCode 医院id
     * @param instrumentNo 探头信息
     * @return
     */
    public InstrumentInfoDto getProbeRedisInfo(String hospitalCode, String instrumentNo) {
        Object instrumentInfoDto = redisUtils.hget("P" + hospitalCode, instrumentNo);
        if(ObjectUtils.isEmpty(instrumentInfoDto)){
            return null;
        }
        return BeanConverter.convert(instrumentInfoDto,InstrumentInfoDto.class);
    }

    /**
     * 添加和修改医院设备探头的redis信息
     * @param instrumentInfoDto
     */
    public void addProbeRedisInfo(InstrumentInfoDto instrumentInfoDto) {
        redisUtils.hset("P"+instrumentInfoDto.getHospitalCode(),instrumentInfoDto.getInstrumentNo()+":"+instrumentInfoDto.getInstrumentConfigId(), JSONUtil.toJsonStr(instrumentInfoDto));
    }


    /**
     * 移除医院redis信息
     * @param hospitalCode
     * @param instrumentNo
     */
    public void removeProbeRedisInfo(String hospitalCode, String instrumentNo) {
        redisUtils.hdel("P"+hospitalCode,instrumentNo);
    }
}
