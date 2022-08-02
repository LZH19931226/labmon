package com.hc.exchange;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;

public interface BaoJinMsg {



	String EXCHANGE_NAME="BJMSCT";

	String EXCHANGE_NAME1="BJMSCTO";

	String EXCHANGE_NAME2="BJMSCTB";

	String EXCHANGE_NAME4 = "TIMEOUT";


	@Input(EXCHANGE_NAME4)
	MessageChannel  getIutputChannel4();

	@Input(EXCHANGE_NAME)
	MessageChannel  getIutputChannel1();

	@Input(EXCHANGE_NAME1)
	MessageChannel  getIutputChannel2();


	@Input(EXCHANGE_NAME2)
	MessageChannel  getIutputChannel3();

}
