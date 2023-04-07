package com.hc.listenter;

import com.hc.exchange.SocketInputMessage;
import com.hc.my.common.core.redis.dto.ParamaterModel;
import com.hc.my.common.core.util.UniqueHash;
import com.hc.service.MTOnlineBeanService;
import com.hc.service.MessagePushService;
import com.hc.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Random;


@Component
@EnableBinding(SocketInputMessage.class)
@Slf4j
public class SocketMessageListener {
    @Autowired
    private MTOnlineBeanService service;
    @Autowired
    private MessagePushService messagePushService;

    @StreamListener(SocketInputMessage.EXCHANGE_NAME)
    public void onMessage(String messageContent) {
        log.info("数据解析服务订阅到的报文:{}", messageContent);
        try {
            ParamaterModel model = JsonUtil.toBean(messageContent, ParamaterModel.class);
            String data = model.getData();
            String logId = model.getLogId();
            //获取到的原始数据进行解析
            ParamaterModel paseData = service.paseData(data);
            if (null == paseData) {
                log.info("无法解析该数据:{}", JsonUtil.toJson(model));
                return;
            }
            paseData.setNowTime(new Date());
            paseData.setData(data);
            paseData.setLogId(logId);
            //推送mq
            randomPush(paseData);
            log.info("数据解析服务解析完成推送到队列:{}", JsonUtil.toJson(paseData));
        } catch (Exception e) {
            return;
        }
    }


    public void randomPush(ParamaterModel model) {
        //生成全局id标记
        int a = new Random().nextInt(3);
        if (a == 0) {
            messagePushService.pushMessage(JsonUtil.toJson(model));
        }
        if (a == 1) {
            messagePushService.pushMessage1(JsonUtil.toJson(model));
        }
        if (a == 2) {
            messagePushService.pushMessage2(JsonUtil.toJson(model));
        }
    }


}
