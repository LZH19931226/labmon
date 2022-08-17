package com.hc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hc.model.WarningModel;
import com.hc.my.common.core.redis.dto.HospitalInfoDto;
import com.hc.my.common.core.redis.dto.UserRightRedisDto;
import com.hc.po.Sendrecord;
import com.hc.po.Userright;

import java.util.List;

public interface SendrecordService extends IService<Sendrecord> {

    void  pushNotification(List<UserRightRedisDto> list, WarningModel warningModel, HospitalInfoDto hospitalInfoDto);

    void  pushTimeOutNotification(List<Userright> userrights, String hospitalName, String eqTypeName,String count);
}
