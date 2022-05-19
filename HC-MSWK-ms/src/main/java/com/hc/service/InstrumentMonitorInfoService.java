package com.hc.service;

import com.hc.model.WarningMqModel;
import com.hc.my.common.core.redis.dto.ParamaterModel;
import com.hc.my.common.core.redis.dto.SnDeviceDto;

import java.util.List;


public interface InstrumentMonitorInfoService {
	
	/*
	 * 数据存储
	 */

	List<WarningMqModel> save(ParamaterModel model, SnDeviceDto monitorinstrument);
}
