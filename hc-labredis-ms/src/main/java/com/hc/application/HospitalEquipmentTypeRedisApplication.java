package com.hc.application;

import com.hc.application.config.RedisUtils;
import com.hc.my.common.core.redis.dto.HospitalEquipmentTypeInfoModel;
import com.hc.my.common.core.redis.namespace.LabManageMentServiceEnum;
import com.hc.my.common.core.util.BeanConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HospitalEquipmentTypeRedisApplication {

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 获取医院设备类型缓存信息
     * @param hospitalCode 医院id
     * @param hospitalEquipmentTypeId 医院设备类型id
     * @return 医院设备类型缓存信息
     */
    public HospitalEquipmentTypeInfoModel findHospitalEquipmentTypeRedisInfo(String hospitalCode, String hospitalEquipmentTypeId) {
        if(!redisUtils.hHasKey(LabManageMentServiceEnum.E.getCode()+hospitalCode,hospitalEquipmentTypeId)){
            return null;
        }
        Object o = redisUtils.hget(LabManageMentServiceEnum.E.getCode() + hospitalCode, hospitalEquipmentTypeId);
        return BeanConverter.convert((String)o,HospitalEquipmentTypeInfoModel.class);
    }
}
