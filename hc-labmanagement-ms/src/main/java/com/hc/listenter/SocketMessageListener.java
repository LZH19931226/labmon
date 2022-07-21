package com.hc.listenter;

import cn.hutool.json.JSONUtil;
import com.hc.dto.InstrumentparamconfigDTO;
import com.hc.dto.MonitorEquipmentDto;
import com.hc.exchanage.EquipmentStateMsg;
import com.hc.my.common.core.constant.enums.SysConstants;
import com.hc.my.common.core.esm.EquipmentState;
import com.hc.service.InstrumentparamconfigService;
import com.hc.service.MonitorEquipmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

/**
 * @author hc
 */
@Component
@EnableBinding(EquipmentStateMsg.class)
@Slf4j
public class SocketMessageListener {

    @Autowired
    private MonitorEquipmentService monitorEquipmentService;

    @Autowired
    private InstrumentparamconfigService instrumentparamconfigService;

    @StreamListener(EquipmentStateMsg.EQUIPMENT_STATE_INFO)
    public void onMessage(String messageContent) {
        log.info("数据处理中心服务接收到数据:" + messageContent);
        process(messageContent);
    }

    private void process(String messageContent) {
        EquipmentState equipmentState = JSONUtil.toBean(messageContent, EquipmentState.class);
        String state = equipmentState.getState();
        String instrumentNo = equipmentState.getInstrumentNo();
        String equipmentNo = equipmentState.getEquipmentNo();
        String instrumentConfigNo = equipmentState.getInstrumentConfigNo();
        switch (state){
            //修改探头信息为报警中
            case SysConstants.IN_ALARM:
                InstrumentparamconfigDTO instrumentparamconfigDTO = new InstrumentparamconfigDTO();
                instrumentparamconfigDTO.setInstrumentparamconfigno(instrumentConfigNo)
                                .setState(state);
                instrumentparamconfigService.updateInfo(instrumentparamconfigDTO);
                //判断设备是否在报警中
                MonitorEquipmentDto monitorEquipmentDto = monitorEquipmentService.selectMonitorEquipmentInfoByNo(equipmentNo);
                if (!SysConstants.IN_ALARM.equals(monitorEquipmentDto.getState())) {
                    monitorEquipmentDto.setState(state);
                    monitorEquipmentService.updateMonitorEquipment(monitorEquipmentDto);
                }
                break;
            //修改探头信息为正常
            case SysConstants.NORMAL:
                InstrumentparamconfigDTO instrumentParamConfigInfo = instrumentparamconfigService.selectInstrumentparamconfigInfo(instrumentConfigNo);
                if(SysConstants.NORMAL.equals(instrumentParamConfigInfo.getState())){
                    return;
                }
                instrumentParamConfigInfo.setState(state);
                instrumentparamconfigService.updateInfo(instrumentParamConfigInfo);
                //判断还有没有探头在报警 没有时修改设备报警信息为正常
                int count = instrumentparamconfigService.selectProbeStateCount(instrumentNo);
                if(count <= 0){
                    MonitorEquipmentDto monitorEquipment = new MonitorEquipmentDto();
                    monitorEquipment.setEquipmentNo(equipmentNo);
                    monitorEquipment.setState(state);
                    monitorEquipmentService.updateMonitorEquipment(monitorEquipment);
                }
                break;
            default:
                break;

        }
    }
}
