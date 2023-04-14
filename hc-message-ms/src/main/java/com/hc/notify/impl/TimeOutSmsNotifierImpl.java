package com.hc.notify.impl;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.hc.model.Notification;
import com.hc.notify.Notifier;
import com.hc.service.SendMesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("TIMEOUTSMS")
public class TimeOutSmsNotifierImpl implements Notifier {

    @Autowired
    private SendMesService sendMesService;

    @Override
    public String exec(Notification notification) {
        //电话号码
        String publishKey = notification.getPublishKey();
        //设备名称
        String messageTitle = notification.getMessageTitle();
        //设备单位
        String messageCover = notification.getMessageCover();
        //设备值
        String messageIntro = notification.getMessageIntro();
        Map<String, String> param = notification.getParam();
        SendSmsResponse sendSmsResponse = sendMesService.sendMes1(publishKey, messageTitle, messageCover, messageIntro, param.get("timeout"));
        return sendSmsResponse.getCode();
    }
}
