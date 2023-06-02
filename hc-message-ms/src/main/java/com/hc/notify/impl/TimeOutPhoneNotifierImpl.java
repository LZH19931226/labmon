package com.hc.notify.impl;

import com.hc.model.Notification;
import com.hc.notify.Notifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("TIMEOUTPHONE")
public class TimeOutPhoneNotifierImpl implements Notifier {



    @Override
    public String exec(Notification notification) {
        String publishKey = notification.getPublishKey();
        //设备名称
        String messageTitle = notification.getMessageTitle();
        //医院名称
        String messageIntro = notification.getMessageIntro();

        return null;
    }
}
