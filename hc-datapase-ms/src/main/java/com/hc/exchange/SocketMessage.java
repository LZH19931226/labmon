package com.hc.exchange;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
public interface SocketMessage {

	String EXCHANGE_NAME="SocketMessage";

	String EXCHANGE_NAME2="SocketMessageF";

	String EXCHANGE_NAME3="SocketMessageS";


//	String EXCHANGE_NAME4="TCPMESSAGE";
//
//	String EXCHANGE_NAME5="TCPMESSAGEF";
//
//	String EXCHANGE_NAME6="TCPMESSAGES";

	@Output(EXCHANGE_NAME)
	MessageChannel  getOutputChannel();

	@Output(EXCHANGE_NAME2)
	MessageChannel  getOutputChannel1();

	@Output(EXCHANGE_NAME3)
	MessageChannel  getOutputChannel2();

//	@Output(EXCHANGE_NAME4)
//	MessageChannel  getOutputChannel3();
//
//
//	@Output(EXCHANGE_NAME5)
//	MessageChannel  getOutputChannel4();
//
//
//	@Output(EXCHANGE_NAME6)
//	MessageChannel  getOutputChannel5();


}
