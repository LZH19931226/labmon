package com.hc.exchanage;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @author hc
 */
public interface EquipmentStateMsg {

    String EQUIPMENT_STATE_INFO = "ESI";

    @Output(EQUIPMENT_STATE_INFO)
    MessageChannel getOutputChannel();
}
