package com.hc.application;

import cn.hutool.json.JSONUtil;
import com.hc.config.RedisUtils;
import com.hc.labmanagent.ProbeInfoApi;
import com.hc.my.common.core.redis.dto.InstrumentInfoDto;
import com.hc.my.common.core.redis.namespace.LabManageMentServiceEnum;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.vo.equimenttype.InstrumentmonitorVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProbeRedisApplication {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ProbeInfoApi probeInfoApi;

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
        List<InstrumentmonitorVo> result = probeInfoApi.selectInstrumentMonitorInfo().getResult();
        List<String> stringList = result.stream().map(InstrumentmonitorVo::getHospitalcode).collect(Collectors.toList());
        stringList.forEach(res->{
                if(redisUtils.hasKey(LabManageMentServiceEnum.P.getCode()+res)){
                    redisUtils.hdel(LabManageMentServiceEnum.P.getCode()+res);
                }
        });
        List<InstrumentInfoDto> convert = BeanConverter.convert(result, InstrumentInfoDto.class);
        for (InstrumentInfoDto instrumentInfoDto : convert) {
            addProbeRedisInfo(instrumentInfoDto);
        }
    }
}
