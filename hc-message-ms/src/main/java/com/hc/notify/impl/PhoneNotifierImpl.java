package com.hc.notify.impl;

import com.aliyuncs.dyvmsapi.model.v20170525.SingleCallByTtsResponse;
import com.hc.model.Notification;
import com.hc.my.common.core.constant.enums.PushType;
import com.hc.notify.Notifier;
import com.hc.service.SendMesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("PHONE")
public class PhoneNotifierImpl implements Notifier {

    @Autowired
    private SendMesService sendMesService;

    @Override
    public String exec(Notification notification)  {
        String publishKey = notification.getPublishKey();
        //设备名称
        String messageTitle = notification.getMessageTitle();
        //医院名称
        String messageIntro = notification.getMessageIntro();
        //推送类型
        String messageBodys = notification.getMessageBodys();
        //通过参数去定义使用短信模板 普通报警短信,超时报警短信
        if (messageBodys.equals(PushType.USUAL_ALARM.name())){
            SingleCallByTtsResponse singleCallByTtsResponse = sendMesService.callPhone(publishKey, messageTitle);
            return singleCallByTtsResponse.getCode();
        }
        if (messageBodys.equals(PushType.TIMEOUT_ALARM.name())){
            SingleCallByTtsResponse singleCallByTtsResponse = sendMesService.callPhone2(publishKey, messageIntro, messageTitle);
            return singleCallByTtsResponse.getCode();
        }

        return null;
    }
}
