package com.hc.service;

import com.hc.clickhouse.po.Warningrecord;
import com.hc.model.WarningModel;
import com.hc.my.common.core.bean.InstrumentMonitorInfoModel;
import com.hc.my.common.core.domain.WarningAlarmDo;
import com.hc.my.common.core.redis.dto.HospitalInfoDto;
import com.hc.my.common.core.redis.dto.InstrumentInfoDto;

/**
 * Created by 16956 on 2018-08-10.
 */
public interface WarningRuleService {

    WarningModel warningRule(HospitalInfoDto hospitalInfoDto, Warningrecord warningrecord, InstrumentInfoDto probe, WarningAlarmDo warningAlarmDo);

}
