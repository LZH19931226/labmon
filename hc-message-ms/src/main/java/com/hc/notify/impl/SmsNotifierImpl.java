package com.hc.notify.impl;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.hc.model.Notification;
import com.hc.my.common.core.constant.enums.PushType;
import com.hc.notify.Notifier;
import com.hc.service.SendMesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("SMS")
public class SmsNotifierImpl implements Notifier {

    @Autowired
    private SendMesService sendMesService;

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

//        String messageBodys = notification.getMessageBodys();
//
//        Map<String, String> param = notification.getParam();

        SendSmsResponse sendSmsResponse = sendMesService.sendMes(publishKey, messageTitle, messageCover, messageIntro);
        return sendSmsResponse.getCode();

        //通过参数去定义使用短信模板 普通报警短信,超时报警短信
//        if (messageBodys.equals(PushType.USUAL_ALARM.name())){
//            SendSmsResponse sendSmsResponse = sendMesService.sendMes(publishKey, messageTitle, messageCover, messageIntro);
//            return sendSmsResponse.getCode();
//        }
//        if (messageBodys.equals(PushType.TIMEOUT_ALARM.name())){
//            SendSmsResponse sendSmsResponse = sendMesService.sendMes1(publishKey, messageTitle, messageCover, messageIntro,param.get("timeout"));
//            return sendSmsResponse.getCode();
//        }
    }
}
