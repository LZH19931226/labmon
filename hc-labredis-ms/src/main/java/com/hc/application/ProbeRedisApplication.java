package com.hc.application;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.hc.application.config.RedisUtils;
import com.hc.hospital.HospitalInfoApi;
import com.hc.labmanagent.ProbeInfoApi;
import com.hc.my.common.core.redis.command.ProbeCommand;
import com.hc.my.common.core.redis.command.ProbeRedisCommand;
import com.hc.my.common.core.redis.dto.InstrumentInfoDto;
import com.hc.my.common.core.redis.dto.InstrumentmonitorDto;
import com.hc.my.common.core.redis.dto.ProbeInfoDto;
import com.hc.my.common.core.redis.dto.WarningRecordDto;
import com.hc.my.common.core.redis.namespace.LabManageMentServiceEnum;
import com.hc.my.common.core.util.BeanConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ProbeRedisApplication {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ProbeInfoApi probeInfoApi;

    @Autowired
    private HospitalInfoApi hospitalInfoApi;

    /**
     * 获取医院探头缓存信息
     * @param hospitalCode 医院id
     * @param instrumentNo 探头信息
     * @return
     */
    public InstrumentInfoDto getProbeRedisInfo(String hospitalCode, String instrumentNo) {
        Object instrumentInfoDto = redisUtils.hget(LabManageMentServiceEnum.P.getCode() + hospitalCode, instrumentNo);
        if(ObjectUtils.isEmpty(instrumentInfoDto)){
            return null;
        }
       return JSONUtil.toBean((String) instrumentInfoDto, InstrumentInfoDto.class);
    }

    /**
     * 添加和修改医院设备探头的redis信息
     * @param instrumentInfoDto
     */
    public void addProbeRedisInfo(InstrumentInfoDto instrumentInfoDto) {
        redisUtils.hset(LabManageMentServiceEnum.P.getCode()+instrumentInfoDto.getHospitalCode(),instrumentInfoDto.getInstrumentNo()+":"+instrumentInfoDto.getInstrumentConfigId(), JSONUtil.toJsonStr(instrumentInfoDto));
    }

    /**
     * 批量更新探头缓存信息
     * */
    public void bulkUpdateProbeRedisInfo(ProbeCommand probeCommand) {
        String hospitalCode = probeCommand.getHospitalCode();
        List<InstrumentInfoDto> instrumentInfoDtoList = probeCommand.getInstrumentInfoDtoList();
        Map<String,Object> map = new HashMap<>();
        for (InstrumentInfoDto instrumentInfoDto : instrumentInfoDtoList) {
            map.put(instrumentInfoDto.getInstrumentNo()+":"+instrumentInfoDto.getInstrumentConfigId(),JSONUtil.toJsonStr(instrumentInfoDto));
        }
        redisUtils.hmset(LabManageMentServiceEnum.P.getCode()+hospitalCode,map);

    }

    /***
     * 批量获取探头信息
     * @param probeCommand
     * @return
     */
    public List<InstrumentInfoDto> bulkGetProbeRedisInfo(ProbeCommand probeCommand) {
        String hospitalCode = probeCommand.getHospitalCode();
        List<String> instrumentNo = probeCommand.getInstrumentNo();
        List<Object> objects = redisUtils.multiGet(LabManageMentServiceEnum.P.getCode() + hospitalCode, instrumentNo);
        if (CollectionUtils.isEmpty(objects)) {
            return null;
        }
        List<InstrumentInfoDto> list =  new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            if(!StringUtils.isEmpty(objects.get(i))){
                InstrumentInfoDto instrumentInfoDto = JSON.parseObject((String) JSON.toJSON(objects.get(i)), new TypeReference<InstrumentInfoDto>() { });
                list.add(instrumentInfoDto);
            }
        }
        return list;
    }

    /**
     * 移除医院redis信息
     * @param hospitalCode
     * @param instrumentNo
     */
    public void removeProbeRedisInfo(String hospitalCode, String instrumentNo) {
        redisUtils.hdel(hospitalCode,instrumentNo);
    }

    /**
     * 同步探头信息
     * 先删除在存入redis
     */
    public void probeRedisInfoCache() {
        List<String> hospitalCodeList = hospitalInfoApi.findHospitalCodeList().getResult();
        for (String hospitalCode : hospitalCodeList) {
            if(redisUtils.hasKey(LabManageMentServiceEnum.P.getCode()+hospitalCode)){
                redisUtils.hDel(LabManageMentServiceEnum.P.getCode()+hospitalCode);
            }
            List<InstrumentmonitorDto> instrumentmonitorDtos = probeInfoApi.selectInstrumentMonitorInfo(hospitalCode).getResult();
            if(CollectionUtils.isEmpty(instrumentmonitorDtos)){
                continue;
            }
            List<InstrumentInfoDto> convert = BeanConverter.convert(instrumentmonitorDtos, InstrumentInfoDto.class);
            Map<String,Object> hashMap = new HashMap<>();
            for (InstrumentInfoDto instrumentInfoDto : convert) {
                //addProbeRedisInfo(instrumentInfoDto);
                hashMap.put( instrumentInfoDto.getInstrumentNo()+":"+instrumentInfoDto.getInstrumentConfigId(), JSONUtil.toJsonStr(instrumentInfoDto));
            }
            redisUtils.hmset(LabManageMentServiceEnum.P.getCode()+hospitalCode,hashMap);
        }
    }

    /**
     * 获取探头报警信息
     * @param hospitalCode 医院id
     * @param instrumentParamConfigNo 探头
     * @return 报警记录集合
     */
    public List<WarningRecordDto> getProbeWarnInfo(String hospitalCode, String instrumentParamConfigNo) {
        Object object = redisUtils.hget(LabManageMentServiceEnum.W.getCode() + hospitalCode, instrumentParamConfigNo);
        if(StringUtils.isEmpty(object)){
            return null;
        }
        return JSON.parseObject((String) JSONObject.toJSON(object), new TypeReference<List<WarningRecordDto>>(){});
    }

    /**
     * 添加探头报警记录
     * @param warningRecordDto 报警记录
     */
    public void addProbeWarnInfo(WarningRecordDto warningRecordDto) {
        String hospitalCode = warningRecordDto.getHospitalcode();
        String instrumentParamConfigNo = warningRecordDto.getInstrumentparamconfigNO();
        if(redisUtils.hHasKey(LabManageMentServiceEnum.W.getCode()+ hospitalCode, instrumentParamConfigNo)){
            List<WarningRecordDto> probeWarnInfo = getProbeWarnInfo( hospitalCode, instrumentParamConfigNo);
            probeWarnInfo.add(warningRecordDto);
            redisUtils.hset(LabManageMentServiceEnum.W.getCode()+ hospitalCode,instrumentParamConfigNo,JSONUtil.toJsonStr(probeWarnInfo));
        }else {
            List<WarningRecordDto> warningRecordDtoList = Collections.singletonList(warningRecordDto);
            redisUtils.hset(LabManageMentServiceEnum.W.getCode()+ hospitalCode,instrumentParamConfigNo,JSONUtil.toJsonStr(warningRecordDtoList));
        }
     }

    /**
     * 移除指定的探头信息
     * @param hospitalCode 医院id
     * @param instrumentParamConfigNo 探头监控信息id
     */
     public void removeProbeWarnInfo(String hospitalCode, String instrumentParamConfigNo){
         if(redisUtils.hHasKey(LabManageMentServiceEnum.W.getCode()+hospitalCode,instrumentParamConfigNo)){
             redisUtils.hdel(LabManageMentServiceEnum.W.getCode()+ hospitalCode,instrumentParamConfigNo);
         }
     }

    /**
     * 判断报警记录是否存在
     * @param hospitalCode
     * @param instrumentParamConfigNo
     * @return
     */
    public boolean hasKey(String hospitalCode, String instrumentParamConfigNo) {
        return redisUtils.hHasKey(LabManageMentServiceEnum.W.getCode()+ hospitalCode,instrumentParamConfigNo);
    }

    /**
     * 获取探头当前值信息
     * @param hospitalCode 医院id
     * @param equipmentNo 设备id
     * @return
     */
    public List<ProbeInfoDto> getCurrentProbeValueInfo(String hospitalCode, String equipmentNo) {
        Object obj = redisUtils.hget(hospitalCode, equipmentNo);
        if(ObjectUtils.isEmpty(obj)){
            return null;
        }
        return JSON.parseObject((String) JSONObject.toJSON(obj), new TypeReference<List<ProbeInfoDto>>(){});
    }

    /**
     * 获取设备监测信息id(用于查询导出做标题)
     * @param hospitalCode
     * @param equipmentNo
     * @return
     */
    public List<Integer> getEquipmentMonitoringInfo(String hospitalCode,String equipmentNo){
        List<ProbeInfoDto> currentProbeValueInfo = getCurrentProbeValueInfo(hospitalCode, equipmentNo);
        return  currentProbeValueInfo.stream().map(ProbeInfoDto::getInstrumentConfigId).collect(Collectors.toList());
    }

    /**
     * 新增或更新探头当前值信息
     * @param probeInfoDto
     */
    public void addCurrentProbeValueInfo(ProbeInfoDto probeInfoDto) {
        if (ObjectUtils.isEmpty(probeInfoDto)) {
            return;
        }
        String hospitalCode = probeInfoDto.getHospitalCode();
        String equipmentNo = probeInfoDto.getEquipmentNo();
        Integer instrumentConfigId = probeInfoDto.getInstrumentConfigId();
        String eName = probeInfoDto.getProbeEName();
        if(redisUtils.hHasKey(hospitalCode,equipmentNo)){
            Object object = redisUtils.hget(hospitalCode, equipmentNo);
            List<ProbeInfoDto> probeInfoDTO = JSON.parseObject((String) JSONObject.toJSON(object), new TypeReference<List<ProbeInfoDto>>(){});
            if (!CollectionUtils.isEmpty(probeInfoDTO)) {
                List<ProbeInfoDto> removeList = new ArrayList<>();
                for (ProbeInfoDto infoDto : probeInfoDTO) {
                    Integer configId = infoDto.getInstrumentConfigId();
                    String probeEName = infoDto.getProbeEName();
                    //通常情况：判断id和Ename相同时 过滤点
                    //特殊情况：id为7时 有两种探头(qc,qcl)但是Ename不同
                    if(Objects.equals(configId, instrumentConfigId) && Objects.equals(probeEName,eName)){
                        removeList.add(infoDto);
                    }
                }
                if(!CollectionUtils.isEmpty(removeList)){
                    probeInfoDTO.removeAll(removeList);
                }
                probeInfoDTO.add(probeInfoDto);
                redisUtils.hset(hospitalCode,equipmentNo,JSONUtil.toJsonStr(probeInfoDTO));
            }
        }else {
            List<ProbeInfoDto> probeInfoDTO = new ArrayList<>();
            probeInfoDTO.add(probeInfoDto);
            redisUtils.hset(hospitalCode,equipmentNo,JSONUtil.toJsonStr(probeInfoDTO));
        }
    }

    public Map<String,List<ProbeInfoDto>> getTheCurrentValueOfTheProbeInBatches(ProbeRedisCommand probeRedisCommand) {
        String hospitalCode = probeRedisCommand.getHospitalCode();
        List<String> eNoList = probeRedisCommand.getENoList();
        List<Object> objects = redisUtils.multiGet(hospitalCode, eNoList);
        if (CollectionUtils.isEmpty(objects)) {
            return null;
        }
        Map<String,List<ProbeInfoDto>> map = new HashMap<>();
        for (int i = 0; i < objects.size(); i++) {
            if(!StringUtils.isEmpty(objects.get(i))){
                List<ProbeInfoDto> list = JSON.parseObject((String) JSON.toJSON(objects.get(i)), new TypeReference<List<ProbeInfoDto>>() {});
                if(!CollectionUtils.isEmpty(list)){
                    String equipmentNo = list.get(0).getEquipmentNo();
                    map.put(equipmentNo,list);
                }
            }
        }
        return map;
    }


}
