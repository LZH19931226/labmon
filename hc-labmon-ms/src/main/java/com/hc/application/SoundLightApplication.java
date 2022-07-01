package com.hc.application;

import com.hc.labmanagent.MonitorEquipmentApi;
import com.hc.my.common.core.util.SoundLightUtils;
import com.hc.my.common.core.redis.dto.SnDeviceDto;
import com.hc.tcp.SoundLightApi;
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
    /**
     * 关闭声光开关
     * @param hospitalCode
     */
    public void turnOff(String hospitalCode) {
        //通关医院id查询所有的
        List<SnDeviceDto> result = monitorEquipmentApi.getEquipmentNoList(hospitalCode, "6").getResult();
        for (SnDeviceDto snDeviceDto : result) {
            String sn = snDeviceDto.getSn();
            if(StringUtils.isBlank(sn)) continue;
            soundLightApi.sendMsg(sn, SoundLightUtils.TURN_OFF_ROUND_LIGHT_COMMAND);
        }
    }

}
