package com.hc.application;

import cn.hutool.json.JSONUtil;
import com.hc.application.config.RedisUtils;
import com.hc.my.common.core.redis.dto.HospitalEquipmentTypeInfoDto;
import com.hc.my.common.core.redis.namespace.LabManageMentServiceEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

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
    public HospitalEquipmentTypeInfoDto findHospitalEquipmentTypeRedisInfo(String hospitalCode, String hospitalEquipmentTypeId) {
        if(!redisUtils.hHasKey(LabManageMentServiceEnum.E.getCode()+hospitalCode,hospitalEquipmentTypeId)){
            return null;
        }
        Object o = redisUtils.hget(LabManageMentServiceEnum.E.getCode() + hospitalCode, hospitalEquipmentTypeId);
        return JSONUtil.toBean((String) o, HospitalEquipmentTypeInfoDto.class);
    }

    /**
     * 新增或修改医院设备类型缓存信息
     * @param hospitalEquipmentTypeInfoModel 医院设备类型缓存信息
     */
    public void addHospitalEquipmentTypeRedisInfo(HospitalEquipmentTypeInfoDto hospitalEquipmentTypeInfoModel) {
        if(ObjectUtils.isEmpty(hospitalEquipmentTypeInfoModel)){
            return;
        }
        String hospitalcode = hospitalEquipmentTypeInfoModel.getHospitalcode();
        String equipmenttypeid = hospitalEquipmentTypeInfoModel.getEquipmenttypeid();
        if(redisUtils.hHasKey(LabManageMentServiceEnum.E.getCode()+hospitalcode,equipmenttypeid) ){
            redisUtils.hdel(LabManageMentServiceEnum.E.getCode()+hospitalcode,equipmenttypeid);
        }
        redisUtils.hset(LabManageMentServiceEnum.E.getCode()+hospitalcode,equipmenttypeid, JSONUtil.toJsonStr(hospitalEquipmentTypeInfoModel));
    }

    /**
     * 移除医院设备类型缓存信息
     * @param hospitalCode 医院id
     * @param hospitalEquipmentTypeId 医院设备类型id
     */
    public void removeHospitalEquipmentTypeRedisInfo(String hospitalCode, String hospitalEquipmentTypeId) {
        if(StringUtils.isNotBlank(hospitalCode) && StringUtils.isNotBlank(hospitalEquipmentTypeId)
                && redisUtils.hHasKey(LabManageMentServiceEnum.E.getCode()+hospitalCode,hospitalEquipmentTypeId) ){
            redisUtils.hdel(LabManageMentServiceEnum.E.getCode()+hospitalCode,hospitalEquipmentTypeId);
        }
    }
}
