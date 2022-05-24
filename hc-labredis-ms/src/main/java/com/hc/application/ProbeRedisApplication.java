package com.hc.application;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.hc.application.config.RedisUtils;
import com.hc.hospital.HospitalInfoApi;
import com.hc.labmanagent.ProbeInfoApi;
import com.hc.my.common.core.redis.dto.InstrumentInfoDto;
import com.hc.my.common.core.redis.dto.WarningRecordDto;
import com.hc.my.common.core.redis.namespace.LabManageMentServiceEnum;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.vo.equimenttype.InstrumentmonitorVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

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
        List<String> hospitalCodeList = hospitalInfoApi.findHospitalCodeList();
        for (String hospitalCode : hospitalCodeList) {
            hospitalCodeList.forEach(res->{
                if(redisUtils.hasKey(LabManageMentServiceEnum.P.getCode()+res)){
                    redisUtils.hdel(LabManageMentServiceEnum.P.getCode()+res);
                }
            });
            List<InstrumentmonitorVo> instrumentmonitorVos = probeInfoApi.selectInstrumentMonitorInfo(hospitalCode).getResult();
            List<InstrumentInfoDto> convert = BeanConverter.convert(instrumentmonitorVos, InstrumentInfoDto.class);
            for (InstrumentInfoDto instrumentInfoDto : convert) {
                addProbeRedisInfo(instrumentInfoDto);
            }
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
}
