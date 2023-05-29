package com.hc.notify.impl;

import com.aliyuncs.dyvmsapi.model.v20170525.SingleCallByTtsResponse;
import com.hc.model.Notification;
import com.hc.notify.Notifier;
import com.hc.service.SendMesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("TIMEOUTPHONE")
public class TimeOutPhoneNotifierImpl implements Notifier {

    @Autowired
    private SendMesService sendMesService;

    @Override
    public String exec(Notification notification) {
        String publishKey = notification.getPublishKey();
        //设备名称
        String messageTitle = notification.getMessageTitle();
        //医院名称
        String messageIntro = notification.getMessageIntro();
        SingleCallByTtsResponse singleCallByTtsResponse = sendMesService.callPhone2(publishKey, messageIntro, messageTitle);
        return singleCallByTtsResponse.getCode();
    }
}
