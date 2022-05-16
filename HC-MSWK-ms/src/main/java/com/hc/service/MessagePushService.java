package com.hc.service;

public interface MessagePushService {
	
	//推送报警
	public boolean  pushMessage1(String message);
	
	//推送报警
	public boolean  pushMessage2(String message);

	//推送报警
	public boolean  pushMessage3(String message);


	//推送 数据超时报警服务
	public boolean pushMessage5(String message);








}
