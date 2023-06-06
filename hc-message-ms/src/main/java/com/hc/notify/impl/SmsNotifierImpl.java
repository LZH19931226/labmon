package com.hc.notify.impl;

import com.hc.model.Notification;
import com.hc.notify.Notifier;
import com.hc.util.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("SMS")
public class SmsNotifierImpl implements Notifier {


    @Autowired
    private SendMessage sendMessage;

    @Override
    public String exec(Notification notification) {
        //电话号码
        String publishKey = notification.getPublishKey();
        String messageCover = notification.getMessageCover();
        sendMessage.sendShortMessage(messageCover, publishKey);
        return "ok";


    }
}
