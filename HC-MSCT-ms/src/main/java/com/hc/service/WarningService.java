package com.hc.service;

import com.hc.clickhouse.po.Warningrecord;
import com.hc.model.WarningModel;
import com.hc.my.common.core.domain.WarningAlarmDo;
import com.hc.my.common.core.redis.dto.HospitalInfoDto;
import com.hc.my.common.core.redis.dto.InstrumentInfoDto;
import com.hc.po.Userright;

import java.util.List;


/**
 * Created by 16956 on 2018-08-09.
 */
public interface WarningService {
    //检查探头设备是否超出量程
    Warningrecord checkProbeLowLimit( InstrumentInfoDto probe, WarningAlarmDo warningAlarmDo,HospitalInfoDto hospitalInfoDto);

    Warningrecord pushNotification(List<Userright> list, WarningModel warningModel, HospitalInfoDto hospitalInfoDto);

    void  pushTimeOutNotification(List<Userright> userrights, String hospitalName, String eqTypeName,String count,String hospitalcode);
}
