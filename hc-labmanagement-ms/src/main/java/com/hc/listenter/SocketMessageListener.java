package com.hc.listenter;

import cn.hutool.json.JSONUtil;
import com.hc.device.ProbeRedisApi;
import com.hc.dto.InstrumentparamconfigDTO;
import com.hc.exchanage.EquipmentStateMsg;
import com.hc.my.common.core.constant.enums.SysConstants;
import com.hc.my.common.core.esm.EquipmentState;
import com.hc.my.common.core.redis.dto.InstrumentInfoDto;
import com.hc.service.InstrumentparamconfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author hc
 */
@Component
@EnableBinding(EquipmentStateMsg.class)
@Slf4j
public class SocketMessageListener {


    @Autowired
    private InstrumentparamconfigService instrumentparamconfigService;

    @Autowired
    private ProbeRedisApi probeRedisApi;


    @StreamListener(EquipmentStateMsg.EQUIPMENT_STATE_INFO)
    public void onMessage(String messageContent) {
        log.info("数据处理中心服务接收到数据:" + messageContent);
        process(messageContent);
    }

    private void process(String messageContent) {
        EquipmentState equipmentState = JSONUtil.toBean(messageContent, EquipmentState.class);
        String newState = equipmentState.getState();
        String instrumentConfigNo = equipmentState.getInstrumentConfigNo();
        String instrumentConfigId = equipmentState.getInstrumentConfigId();
        String hospitalCode = equipmentState.getHospitalCode();
        InstrumentInfoDto instrumentInfoDto = probeRedisApi.getProbeRedisInfo(hospitalCode, instrumentConfigNo + ":" + instrumentConfigId).getResult();
        String oldState = instrumentInfoDto.getState();
        if (StringUtils.isEmpty(oldState)){
            oldState="0";
        }
        switch (newState){
            //从不报警变成报警
            case SysConstants.IN_ALARM:
                //判断缓存中的信息是否相同 不相同时更新
                if(SysConstants.IN_ALARM.equals(oldState)){
                    return;
                }
                InstrumentparamconfigDTO instrumentparamconfigDTO = new InstrumentparamconfigDTO();
                instrumentparamconfigDTO.setInstrumentparamconfigno(instrumentConfigNo)
                                .setState(newState);
                instrumentparamconfigService.updateInfo(instrumentparamconfigDTO);
                instrumentInfoDto.setState(newState);
                probeRedisApi.addProbeRedisInfo(instrumentInfoDto);
                break;
            case SysConstants.NORMAL:
                //从报警恢复正常
                //判断缓存中的设备状态是否相同 不同时做更改
                if( SysConstants.NORMAL.equals(oldState)){
                    return;
                }
                //同步更新缓存和数据库
                InstrumentparamconfigDTO instrumentParamConfigInfo = new InstrumentparamconfigDTO();
                instrumentParamConfigInfo.setInstrumentparamconfigno(instrumentConfigNo);
                instrumentParamConfigInfo.setState(newState);
                instrumentparamconfigService.updateInfo(instrumentParamConfigInfo);
                instrumentInfoDto.setState(newState);
                probeRedisApi.addProbeRedisInfo(instrumentInfoDto);
                break;
            default:
                break;

        }
    }
}
