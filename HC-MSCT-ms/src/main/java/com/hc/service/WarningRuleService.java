package com.hc.service;

import com.hc.model.WarningModel;
import com.hc.my.common.core.bean.InstrumentMonitorInfoModel;
import com.hc.my.common.core.redis.dto.InstrumentInfoDto;

/**
 * Created by 16956 on 2018-08-10.
 */
public interface WarningRuleService {

    WarningModel warningRule(String hospitalcode, String pkid, String data, InstrumentInfoDto probe,String logId);

}
