package com.hc.application;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.hc.application.config.RedisUtils;
import com.hc.labmanagent.HospitalEquipmentTypeApi;
import com.hc.my.common.core.redis.dto.HospitalEquipmentTypeInfoDto;
import com.hc.my.common.core.redis.namespace.LabManageMentServiceEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class HospitalEquipmentTypeRedisApplication {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private HospitalEquipmentTypeApi hospitalEquipmentTypeApi;

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

    /**
     * 同步所有的设备类型信息
     */
    public void hospitalEquipmentTypeRedisInfoCache() {
        List<HospitalEquipmentTypeInfoDto> equipmentTypeInfoList = hospitalEquipmentTypeApi.getAllHospitalEquipmentTypeInfo().getResult();
        if (CollectionUtils.isEmpty(equipmentTypeInfoList)) {
            return;
        }
        //清除所有的医院设备信息
        Set<String> hospitalCodeSet = equipmentTypeInfoList.stream().map(HospitalEquipmentTypeInfoDto::getHospitalcode).collect(Collectors.toSet());
        hospitalCodeSet.forEach(hospitalCode->{
            redisUtils.hDel(LabManageMentServiceEnum.E.getCode()+hospitalCode);
        });
        for (HospitalEquipmentTypeInfoDto hospitalEquipmentTypeInfoDto : equipmentTypeInfoList) {
            String hospitalCode = hospitalEquipmentTypeInfoDto.getHospitalcode();
            String equipmentTypeId = hospitalEquipmentTypeInfoDto.getEquipmenttypeid();
            redisUtils.hset(LabManageMentServiceEnum.E.getCode()+hospitalCode,equipmentTypeId,JSONUtil.toJsonStr(hospitalEquipmentTypeInfoDto));
        }
    }

    /**
     * 批量换取设备类型
     * @param hospitalCode
     * @return
     */
    public List<HospitalEquipmentTypeInfoDto> bulkAcquisitionEqType(String hospitalCode){
        List<HospitalEquipmentTypeInfoDto> list = new ArrayList<>();
        for (int i = 1; i < 7; i++) {
            Object obj = redisUtils.hget(LabManageMentServiceEnum.E.getCode() + hospitalCode, String.valueOf(i));
            if(obj!=null){
                HospitalEquipmentTypeInfoDto parseObject = JSON.parseObject(obj.toString(), HospitalEquipmentTypeInfoDto.class);
                list.add(parseObject);
            }
        }
        return list;
    }
}
