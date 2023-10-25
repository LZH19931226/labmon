package com.hc.notify.impl;

import com.hc.model.Notification;
import com.hc.notify.Notifier;
import com.hc.util.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("TIMEOUTSMS")
public class TimeOutSmsNotifierImpl implements Notifier {


    @Autowired
    private SendMessage sendMessage;

    @Override
    public String exec(Notification notification) {
        //电话
        String publishKey = notification.getPublishKey();
        //模板
        String messageTitle = notification.getMessageIntro();
        //报警信息
        String messageCover = notification.getMessageCover();
        sendMessage.sendShortMessage(messageCover, publishKey);
        return "OK";

    }
}
