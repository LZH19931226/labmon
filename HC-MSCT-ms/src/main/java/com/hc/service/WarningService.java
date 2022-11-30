package com.hc.service;

import com.hc.clickhouse.po.Warningrecord;
import com.hc.my.common.core.domain.WarningAlarmDo;
import com.hc.model.WarningModel;
import com.hc.my.common.core.redis.dto.InstrumentInfoDto;

import java.util.List;


/**
 * Created by 16956 on 2018-08-09.
 */
public interface WarningService {
    //检查探头设备是否超出量程
    Warningrecord checkProbeLowLimit( InstrumentInfoDto probe, WarningAlarmDo warningAlarmDo);
}
