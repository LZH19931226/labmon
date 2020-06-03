package com.hc.service;

import com.hc.bean.ParamaterModel;
import com.hc.entity.Monitorinstrument;

public interface MonitorinstrumentService {
	/*
	 * 通过sn号注册设备
	 */
    Monitorinstrument saveMonitorinstrument(String sn, String mt600sn, ParamaterModel paramaterModel);
}
