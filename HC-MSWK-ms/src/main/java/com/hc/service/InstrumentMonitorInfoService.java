package com.hc.service;

import com.hc.bean.WarningMqModel;
import com.hc.entity.Monitorinstrument;
import com.hc.my.common.core.bean.ParamaterModel;

import java.util.List;

public interface InstrumentMonitorInfoService {
	
	/*
	 * 数据存储
	 */

	List<WarningMqModel> save(ParamaterModel model, Monitorinstrument monitorinstrument);
}
