package com.hc.serviceImpl;

import com.hc.model.MapperModel.TimeoutEquipment;
import com.hc.service.MessagePushService;
import com.hc.service.ThirdPartyService;
import com.hc.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author LiuZhiHao
 * @date 2020/7/9 10:44
 * 描述:
 **/
@Service
@Slf4j
public class ThirdPartyServiceImpl implements ThirdPartyService {

    @Autowired
    private MessagePushService messagePushService;

    @Override
    public void disableAlarm(TimeoutEquipment timeoutEquipment) {
        timeoutEquipment.setDisabletype("2");
        String s = JsonUtil.toJson(timeoutEquipment);
        log.info("超时报警推送:{}",JsonUtil.toJson(timeoutEquipment));
        messagePushService.pushMessage5(s);
    }
}
