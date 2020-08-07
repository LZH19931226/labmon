package com.hc.serviceimpl;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.dyvmsapi.model.v20170525.SingleCallByTtsResponse;
import com.hc.Message.MoblieMessageUtil;
import com.hc.Message.SingleCallByTtsUtils;
import com.hc.service.SendMesService;
import com.hc.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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
        SendSmsResponse sendmsg = moblieMessageUtil.sendmsg(phonenum, equipmentname, unit, value);
        log.info("发送短信:{},响应值:{}",phonenum,sendmsg.getCode());
        return sendmsg;
    }

    @Override
    public SendSmsResponse sendMes1(String phonenum, String equipmentname, String unit, String value, int time) {
        SendSmsResponse sendmsg = moblieMessageUtil.sendmsg1(phonenum, equipmentname, unit, value,time);

        return sendmsg;
    }

    @Override
    public SendSmsResponse timingsms(String total, String normaltotal, String abnormal,String phone,String type) {
        SendSmsResponse sendmsg = moblieMessageUtil.timingsms(total, normaltotal, abnormal,phone,type);

        return sendmsg;
    }

    @Override
    @Async
    public SingleCallByTtsResponse callPhone(String phone, String equipmentname) {
        SingleCallByTtsResponse singleCallByTtsResponse = singleCallByTtsUtils.sendSms(phone, equipmentname);
        log.info("拨打电话对象:{},响应值:{}",phone,singleCallByTtsResponse.getCode());
        return singleCallByTtsResponse;
    }

    @Override
    @Async
    public void sendYmMessage(String pkid) {
        if (StringUtils.isNotEmpty(pkid)) {
            //推送APP
            Map<String, String> map = new HashMap<String, String>();
            map.put("pkid", pkid);
            HttpUtil.get("http://www.sosum.net:8087/api-mon/api/insParamSet/sendMessage", map);
        }
    }

    @Override
    @Async
    public SingleCallByTtsResponse receivePhone(String phone) {
        SingleCallByTtsResponse singleCallByTtsResponse = singleCallByTtsUtils.sendSmsReceive(phone);
        log.info("回拨电话对象:{},响应值:{}",phone,singleCallByTtsResponse.getCode());
        return singleCallByTtsResponse;
    }

    @Override
    public SendSmsResponse sendCode(String phonenum, String code) {
        SendSmsResponse sendSmsResponse = moblieMessageUtil.senCode(phonenum, code);
        return sendSmsResponse;
    }
}
