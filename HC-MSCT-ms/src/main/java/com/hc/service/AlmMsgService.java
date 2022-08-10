package com.hc.service;

import com.hc.clickhouse.po.Warningrecord;
import com.hc.my.common.core.domain.MonitorinstrumentDo;
import com.hc.my.common.core.redis.dto.UserRightRedisDto;

import java.util.List;

public interface AlmMsgService {

    //获取需要报警通知到得人员信息
    List<UserRightRedisDto> addUserScheduLing(String hospitalcode);

    //通过设备获取设备报警是否处于报警时段之内
    boolean warningTimeBlockRule(MonitorinstrumentDo monitorinstrument, Warningrecord warningrecord);
}
