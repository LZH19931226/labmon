package com.hc.exchange;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;

public interface SocketMessage {

	String EXCHANGE_NAME="SocketMessage";

	String EXCHANGE_NAME2="SocketMessageF";

	String EXCHANGE_NAME3="SocketMessageS";

	String EQUIPMENT_STATE_INFO = "ESI";


	String EXCHANGE_NAME_HAVER="HAVER-MQTT";

	@Input(EXCHANGE_NAME)
	MessageChannel  getIutputChannel();

	@Input(EXCHANGE_NAME2)
	MessageChannel  getIutputChannel1();

	@Input(EXCHANGE_NAME3)
	MessageChannel  getIutputChannel2();

	@Input(EQUIPMENT_STATE_INFO)
	MessageChannel  getChannelInfo();

	@Input(EXCHANGE_NAME_HAVER)
	MessageChannel  getHavrtIutputChannel();
}
