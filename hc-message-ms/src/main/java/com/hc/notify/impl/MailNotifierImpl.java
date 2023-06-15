package com.hc.notify.impl;

import cn.hutool.json.JSONUtil;
import com.hc.model.Notification;
import com.hc.notify.Notifier;
import com.hc.util.AmazonConnectUtil;
import com.hc.util.SendEmailMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("MAIL")
@Slf4j
public class MailNotifierImpl implements Notifier {


    @Autowired
    private SendEmailMessage sendEmailMessage;

    @Override
    public String exec(Notification notification)  {
        String publishKey = notification.getPublishKey();
        String messageCover = notification.getMessageCover();
        sendEmailMessage.emailMessage("hc-ivf",publishKey,messageCover);
        log.info("发送邮件成功:{}", JSONUtil.toJsonStr(notification));
        return  "ok";

    }
}
