package com.hc.listenter;

import cn.hutool.json.JSONUtil;
import com.hc.device.ProbeRedisApi;
import com.hc.device.SnDeviceRedisApi;
import com.hc.dto.InstrumentparamconfigDTO;
import com.hc.dto.MonitorEquipmentDto;
import com.hc.exchanage.EquipmentStateMsg;
import com.hc.my.common.core.constant.enums.SysConstants;
import com.hc.my.common.core.esm.EquipmentState;
import com.hc.my.common.core.redis.dto.InstrumentInfoDto;
import com.hc.my.common.core.redis.dto.SnDeviceDto;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.service.InstrumentparamconfigService;
import com.hc.service.MonitorEquipmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

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

    @Autowired
    private ProbeRedisApi probeRedisApi;

    @Autowired
    private SnDeviceRedisApi snDeviceRedisApi;

    @StreamListener(EquipmentStateMsg.EQUIPMENT_STATE_INFO)
    public void onMessage(String messageContent) {
        log.info("数据处理中心服务接收到数据:" + messageContent);
        process(messageContent);
    }

    private void process(String messageContent) {
        EquipmentState equipmentState = JSONUtil.toBean(messageContent, EquipmentState.class);
        String state = equipmentState.getState();
        String instrumentNo = equipmentState.getInstrumentNo();
        String instrumentConfigNo = equipmentState.getInstrumentConfigNo();
        String instrumentConfigId = equipmentState.getInstrumentConfigId();
        String hospitalCode = equipmentState.getHospitalCode();
        String sn = equipmentState.getSn();
        switch (state){
            case SysConstants.IN_ALARM:
                //判断缓存中的信息是否相同 不相同时更新
                InstrumentInfoDto result = probeRedisApi.getProbeRedisInfo(hospitalCode, instrumentConfigNo + ":" + instrumentConfigId).getResult();
                if(!ObjectUtils.isEmpty(result) && SysConstants.IN_ALARM.equals(result.getState())){
                    return;
                }
                InstrumentparamconfigDTO instrumentparamconfigDTO = new InstrumentparamconfigDTO();
                instrumentparamconfigDTO.setInstrumentparamconfigno(instrumentConfigNo)
                                .setState(state);
                instrumentparamconfigService.updateInfo(instrumentparamconfigDTO);
                result.setState(state);
                probeRedisApi.addProbeRedisInfo(result);
                //判断设备是否在报警中 如果相同则不修改
                SnDeviceDto snDeviceDto = snDeviceRedisApi.getSnDeviceDto(sn).getResult();
                if (!ObjectUtils.isEmpty(snDeviceDto) && SysConstants.IN_ALARM.equals(snDeviceDto.getState())) {
                    return;
                }
                //同步更新缓存和数据库
                snDeviceDto.setState(state);
                snDeviceRedisApi.updateSnDeviceDtoSync(snDeviceDto);
                MonitorEquipmentDto convert = BeanConverter.convert(snDeviceDto, MonitorEquipmentDto.class);
                monitorEquipmentService.updateMonitorEquipment(convert);
                break;
            case SysConstants.NORMAL:
                //判断缓存中的设备状态是否相同 不同时做更改
                InstrumentInfoDto instrumentInfoDto = probeRedisApi.getProbeRedisInfo(hospitalCode, instrumentNo + ":" + instrumentConfigId).getResult();
                if(!ObjectUtils.isEmpty(instrumentInfoDto) && SysConstants.NORMAL.equals(instrumentInfoDto.getState())){
                    return;
                }
                //同步更新缓存和数据库
                InstrumentparamconfigDTO instrumentParamConfigInfo = new InstrumentparamconfigDTO();
                instrumentParamConfigInfo.setInstrumentparamconfigno(instrumentConfigNo);
                instrumentParamConfigInfo.setState(state);
                instrumentparamconfigService.updateInfo(instrumentParamConfigInfo);
                instrumentInfoDto.setState(state);
                probeRedisApi.addProbeRedisInfo(instrumentInfoDto);
                break;
            default:
                break;

        }
    }
}
