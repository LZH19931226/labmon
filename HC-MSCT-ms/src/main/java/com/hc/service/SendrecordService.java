package com.hc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hc.clickhouse.po.Warningrecord;
import com.hc.model.WarningModel;
import com.hc.my.common.core.redis.dto.HospitalInfoDto;
import com.hc.po.Sendrecord;
import com.hc.po.Userright;

import java.util.List;

public interface SendrecordService extends IService<Sendrecord> {

    Warningrecord pushNotification(List<Userright> list, WarningModel warningModel, HospitalInfoDto hospitalInfoDto);

    void  pushTimeOutNotification(List<Userright> userrights, String hospitalName, String eqTypeName,String count);
}
