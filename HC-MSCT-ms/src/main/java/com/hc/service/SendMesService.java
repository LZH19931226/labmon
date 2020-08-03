package com.hc.service;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.dyvmsapi.model.v20170525.SingleCallByTtsResponse;

/**
 * Created by 16956 on 2018-08-13.
 */
public interface SendMesService {

    SendSmsResponse sendMes(String phonenum, String equipmentname, String unit, String value);
    SendSmsResponse sendMes1(String phonenum, String equipmentname, String unit, String value,int time);
    SendSmsResponse timingsms(String total,String normaltotal,String abnormal,String phone,String type);

    SingleCallByTtsResponse callPhone(String phone, String equipmentname);


    void sendYmMessage(String pkid);

    SingleCallByTtsResponse receivePhone(String phone);
    SendSmsResponse sendCode(String phonenum, String code);
}
