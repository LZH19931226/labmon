package com.hc.exchanage;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

/**
 * @author hc
 */
@Component
public interface EquipmentStateMsg {

    String EQUIPMENT_STATE_INFO = "ESI";

    @Input(EQUIPMENT_STATE_INFO)
    MessageChannel getChannelInfo();
}
