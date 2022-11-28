package com.hc.exchange;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
public interface BaoJinMsg {
	

	String EXCHANGE_NAME="HAVER-MQTT";

	@Output(EXCHANGE_NAME)
	MessageChannel getOutputChannel();


}
