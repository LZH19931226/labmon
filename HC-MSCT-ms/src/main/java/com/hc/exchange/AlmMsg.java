package com.hc.exchange;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
public interface AlmMsg {


    String EXCHANGE_NAME="ALMMessageK";

    String EXCHANGE_NAMEG="ALMMessageG";

    @Output(EXCHANGE_NAME)
    MessageChannel getOutputChannel();


    @Output(EXCHANGE_NAMEG)
    MessageChannel getOutputChannel1();
}
