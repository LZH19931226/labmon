package com.hc.exchange;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

/**
 * @author hc
 */
@Component
public interface EquipmentStateMsg {

    //用于修改探头当前是否处于报警得状态
    String EQUIPMENT_STATE_INFO = "ESI";

    @Output(EQUIPMENT_STATE_INFO)
    MessageChannel getOutputChannel();
}
