package com.hc.serviceimpl;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.dyvmsapi.model.v20170525.SingleCallByTtsResponse;
import com.hc.Message.MoblieMessageUtil;
import com.hc.Message.SingleCallByTtsUtils;
import com.hc.service.SendMesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Created by 16956 on 2018-08-13.
 */
@Service
@Slf4j
public class SendMesServiceImpl implements SendMesService {

    @Autowired
    private MoblieMessageUtil moblieMessageUtil;

    @Autowired
    private SingleCallByTtsUtils singleCallByTtsUtils;

    @Override
    public SendSmsResponse sendMes(String phonenum, String equipmentname, String unit, String value) {
        return moblieMessageUtil.sendmsg(phonenum, equipmentname, unit, value);
    }

    @Override
    public SendSmsResponse sendMes1(String phonenum, String equipmentname, String unit, String value, int time) {

        return moblieMessageUtil.sendmsg1(phonenum, equipmentname, unit, value,time);
    }

    @Override
    public SendSmsResponse timingsms(String total, String normaltotal, String abnormal,String phone,String type) {

        return moblieMessageUtil.timingsms(total, normaltotal, abnormal,phone,type);
    }

    @Override
    @Async
    public SingleCallByTtsResponse callPhone(String phone, String equipmentname) {
        return singleCallByTtsUtils.sendSms(phone, equipmentname);
    }

    @Override
    @Async
    public SingleCallByTtsResponse receivePhone(String phone) {
        return singleCallByTtsUtils.sendSmsReceive(phone);
    }

    @Override
    public SendSmsResponse sendCode(String phonenum, String code) {
        return moblieMessageUtil.senCode(phonenum, code);
    }
}
