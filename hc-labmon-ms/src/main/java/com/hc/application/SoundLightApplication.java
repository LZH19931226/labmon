package com.hc.application;

import com.hc.dto.HospitalInfoDto;
import com.hc.labmanagent.MonitorEquipmentApi;
import com.hc.my.common.core.redis.dto.SnDeviceDto;
import com.hc.my.common.core.util.SoundLightUtils;
import com.hc.service.HospitalInfoService;
import com.hc.tcp.SoundLightApi;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SoundLightApplication {

    @Autowired
    private MonitorEquipmentApi monitorEquipmentApi;

    @Autowired
    private SoundLightApi soundLightApi;

    @Autowired
    private HospitalInfoService hospitalInfoService;
    /**
     * 关闭声光开关
     * @param hospitalCode
     */
    public void turnOff(String hospitalCode) {
        //通关医院id查询所有的
        List<SnDeviceDto> result = monitorEquipmentApi.getEquipmentNoList(hospitalCode, "6").getResult();
        if (CollectionUtils.isEmpty(result)) {
            return;
        }
        for (SnDeviceDto snDeviceDto : result) {
            String sn = snDeviceDto.getSn();
            if(StringUtils.isBlank(sn)) {
                continue;
            }
            soundLightApi.sendMsg(sn, SoundLightUtils.TURN_OFF_ROUND_LIGHT_COMMAND);
        }
    }

    /**
     * 获取医院的信息
     * @param hospitalCode 医院id
     * @return 结果
     */
    public HospitalInfoDto getHospitalInfo(String hospitalCode) {
        HospitalInfoDto hospitalInfoDto = hospitalInfoService.selectOne(hospitalCode);
        String soundLightAlarm = hospitalInfoDto.getSoundLightAlarm();
        if(StringUtils.isBlank(soundLightAlarm)){
            hospitalInfoDto.setSoundLightAlarm("0");
        }
        return hospitalInfoDto;
    }
}
