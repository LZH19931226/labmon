package com.hc.application;

import cn.hutool.json.JSONUtil;
import com.hc.application.config.RedisUtils;
import com.hc.hospital.HospitalInfoApi;
import com.hc.my.common.core.redis.dto.HospitalInfoDto;
import com.hc.my.common.core.redis.namespace.LabManageMentServiceEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author hc
 */
@Component
public class HospitalInfoRedisApplication {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private HospitalInfoApi hospitalInfoApi;

    /**
     * 获取医院的缓存信息
     * @param hospitalCode 医院id
     */
    public HospitalInfoDto findHospitalInfo(String hospitalCode) {
        Object o = redisUtils.get(LabManageMentServiceEnum.H.getCode() + hospitalCode);
        return JSONUtil.toBean((String)o,HospitalInfoDto.class);
    }

    /**
     * 新增医院缓存信息
     * @param hospitalInfoDto 医院缓存信息
     */
    public void addHospitalRedisInfo(HospitalInfoDto hospitalInfoDto) {
        redisUtils.set(LabManageMentServiceEnum.H.getCode()+hospitalInfoDto.getHospitalCode(), JSONUtil.toJsonStr(hospitalInfoDto));
    }

    /**
     * 移除医院缓存信息
     * @param hospitalCode 医院id
     */
    public void removeHospitalRedisInfo(String hospitalCode) {
        boolean flag = redisUtils.hasKey(LabManageMentServiceEnum.H.getCode() + hospitalCode);
        if(flag){
            redisUtils.del(LabManageMentServiceEnum.H.getCode()+hospitalCode);
        }
    }

    /**
     * 同步医院信息缓存
     */
    public void hospitalInfoCache() {
        List<HospitalInfoDto> hospitalInfoDtos = hospitalInfoApi.getAllHospitalInfo().getResult();
        if(CollectionUtils.isEmpty(hospitalInfoDtos)){
            return;
        }
        for (HospitalInfoDto hospitalInfoDto : hospitalInfoDtos) {
            if (!ObjectUtils.isEmpty(hospitalInfoDto) && redisUtils.hasKey(LabManageMentServiceEnum.H.getCode()+hospitalInfoDto.getHospitalCode())) {
                redisUtils.del(LabManageMentServiceEnum.H.getCode()+hospitalInfoDto.getHospitalCode());
            }
            redisUtils.set(LabManageMentServiceEnum.H.getCode()+hospitalInfoDto.getHospitalCode(),JSONUtil.toJsonStr(hospitalInfoDto));
        }
    }
}
