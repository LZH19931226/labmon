package com.hc.notify.impl;

import com.hc.model.Notification;
import com.hc.notify.Notifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("TIMEOUTSMS")
public class TimeOutSmsNotifierImpl implements Notifier {



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

        return  null;

    }
}
