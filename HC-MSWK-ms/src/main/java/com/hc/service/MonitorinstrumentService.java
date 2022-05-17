package com.hc.service;

import com.hc.po.Monitorinstrument;
import com.hc.my.common.core.redis.dto.ParamaterModel;

public interface MonitorinstrumentService {
	/*
	 * 通过sn号注册设备
	 */
    Monitorinstrument saveMonitorinstrument(String sn, String mt600sn, ParamaterModel paramaterModel);
}
