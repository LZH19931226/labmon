package com.hc.service;

import com.hc.bean.ParamaterModel;
import com.hc.bean.WarningModel;
import com.hc.bean.WarningMqModel;
import com.hc.entity.Monitorinstrument;

import java.util.List;

public interface InstrumentMonitorInfoService {
	
	/*
	 * 数据存储
	 */

	public List<WarningMqModel> save(ParamaterModel model , Monitorinstrument monitorinstrument);
}
