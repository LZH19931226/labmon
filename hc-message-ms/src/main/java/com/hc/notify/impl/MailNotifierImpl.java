package com.hc.notify.impl;

import com.hc.model.Notification;
import com.hc.notify.Notifier;
import com.hc.util.AmazonConnectUtil;
import com.hc.util.SendEmailMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("MAIL")
public class MailNotifierImpl implements Notifier {


    @Autowired
    private SendEmailMessage sendEmailMessage;

    @Override
    public String exec(Notification notification)  {
        String publishKey = notification.getPublishKey();
        String messageCover = notification.getMessageCover();
        sendEmailMessage.sendShortMessage("Mutual innovation United technology,Alarm notification",publishKey,messageCover);
        return  "ok";

    }
}
