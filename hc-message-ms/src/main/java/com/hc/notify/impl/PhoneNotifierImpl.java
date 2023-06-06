package com.hc.notify.impl;

import com.hc.model.Notification;
import com.hc.notify.Notifier;
import com.hc.util.AmazonConnectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("PHONE")
public class PhoneNotifierImpl implements Notifier {


    @Autowired
    private AmazonConnectUtil amazonConnectUtil;

    @Override
    public String exec(Notification notification)  {
        String publishKey = notification.getPublishKey();
        String messageCover = notification.getMessageCover();
        amazonConnectUtil.makeCall(publishKey,messageCover);
        return  "ok";

    }
}
