package com.hc.exchange;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
public interface BaoJinMsg {
	

	String EXCHANGE_NAME="BJMSCT";


	String EXCHANGE_NAME1="BJMSCTO";

	String EXCHANGE_NAME2="BJMSCTB";



	String EXCHANGE_NAME4 = "TIMEOUT";



	@Output(EXCHANGE_NAME4)
	MessageChannel getOutputChannel5();

	@Output(EXCHANGE_NAME)
	MessageChannel  getOutputChannel1();

	@Output(EXCHANGE_NAME1)
	MessageChannel  getOutputChannel2();

	@Output(EXCHANGE_NAME2)
	MessageChannel  getOutputChannel3();
}
