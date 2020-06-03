package com.hc.service;

/**
 * Created by 16956 on 2018-08-30.
 */

import com.hc.bean.ParamaterModel;
import com.hc.entity.Monitorinstrument;

/**
 * 探头注册到医院服务
 */
public interface MonitorRegisterHospitalService {

    void instrumentRegister(String instrumenttypename, ParamaterModel paramaterModel,Monitorinstrument monitorinstrument,String channel);
}
