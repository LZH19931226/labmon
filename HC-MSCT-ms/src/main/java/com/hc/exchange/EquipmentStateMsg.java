package com.hc.exchange;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

/**
 * @author hc
 */
@Component
public interface EquipmentStateMsg {

    String EQUIPMENT_STATE_INFO = "ESI";

    @Output(EQUIPMENT_STATE_INFO)
    MessageChannel getOutputChannel();
}
