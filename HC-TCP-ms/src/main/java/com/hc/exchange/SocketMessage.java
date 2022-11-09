package com.hc.exchange;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
public interface SocketMessage {

    //定义推送到队列里面的信息,接收到硬件信息,解析出来SN转发给业务服务
	String EXCHANGE_NAME= "BUSINESS_SYSTEM_DATA";


	@Output(EXCHANGE_NAME)
	MessageChannel  getOutputChannel();




}
