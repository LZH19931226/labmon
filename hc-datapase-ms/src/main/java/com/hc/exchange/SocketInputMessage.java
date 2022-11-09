package com.hc.exchange;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;

public interface SocketInputMessage {

	String EXCHANGE_NAME="BUSINESS_SYSTEM_DATA";


	@Input(EXCHANGE_NAME)
	MessageChannel  getIutputChannel();

}
