package com.hc.notify.impl;

import com.hc.model.Notification;
import com.hc.notify.Notifier;
import com.hc.service.SendMesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SMS")
public class SmsNotifierImpl implements Notifier {

    @Autowired
    private SendMesService sendMesService;

    @Override
    public String exec(Notification notification) {

        String publishKey = notification.getPublishKey();
        sendMesService.sendMes(publishKey, "测试", "氧气", "123");
        return null;
    }
}
