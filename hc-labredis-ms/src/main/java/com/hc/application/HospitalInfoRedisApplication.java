package com.hc.application;

import com.hc.application.config.RedisUtils;
import com.hc.my.common.core.redis.dto.HospitalInfoDto;
import com.hc.my.common.core.redis.namespace.LabManageMentServiceEnum;
import com.hc.my.common.core.util.BeanConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author hc
 */
@Component
public class HospitalInfoRedisApplication {

    @Autowired
    private RedisUtils redisUtils;
    public HospitalInfoDto findHospitalInfo(String hospitalCode) {
        Object o = redisUtils.get(LabManageMentServiceEnum.H.getCode() + hospitalCode);
        return BeanConverter.convert((String)o,HospitalInfoDto.class);
    }
}
