package com.hc.exchange;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
public interface SocketOutPutMessage {

    //推送给数据处理中心
       String EXCHANGE_NAME = "SocketMessage";

      String EXCHANGE_NAME2 = "SocketMessageF";

      String EXCHANGE_NAME3 = "SocketMessageS";


    @Output(EXCHANGE_NAME)
     MessageChannel getOutputChannel();

    @Output(EXCHANGE_NAME2)
     MessageChannel getOutputChannel1();

    @Output(EXCHANGE_NAME3)
     MessageChannel getOutputChannel2();


}
