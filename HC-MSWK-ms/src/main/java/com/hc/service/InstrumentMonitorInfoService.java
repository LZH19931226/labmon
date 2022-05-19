package com.hc.service;

import com.hc.po.Monitorinstrument;
import com.hc.model.WarningMqModel;
import com.hc.my.common.core.redis.dto.ParamaterModel;

import java.util.List;


public interface InstrumentMonitorInfoService {
	
	/*
	 * 数据存储
	 */

	List<WarningMqModel> save(ParamaterModel model, Monitorinstrument monitorinstrument);
}
