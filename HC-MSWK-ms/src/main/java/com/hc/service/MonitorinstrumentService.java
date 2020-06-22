package com.hc.service;

import com.hc.entity.Monitorinstrument;
import com.hc.my.common.core.bean.ParamaterModel;

public interface MonitorinstrumentService {
	/*
	 * 通过sn号注册设备
	 */
    Monitorinstrument saveMonitorinstrument(String sn, String mt600sn, ParamaterModel paramaterModel);
}
