package com.hc.service;

import com.hc.bean.WarningModel;
import com.hc.model.ResponseModel.InstrumentMonitorInfoModel;

/**
 * Created by 16956 on 2018-08-10.
 */
public interface WarningRuleService {

    WarningModel warningRule(String hospitalcode, String pkid, String data, InstrumentMonitorInfoModel instrumentMonitorInfoModel,String remark);
}
