package com.hc.serviceimpl;

import com.hc.MessageApi;
import com.hc.clickhouse.po.Warningrecord;
import com.hc.exchange.EquipmentStateMsg;
import com.hc.model.WarningModel;
import com.hc.my.common.core.constant.enums.DictEnum;
import com.hc.my.common.core.constant.enums.ElkLogDetail;
import com.hc.my.common.core.constant.enums.NotifyChannel;
import com.hc.my.common.core.constant.enums.PushType;
import com.hc.my.common.core.domain.P2PNotify;
import com.hc.my.common.core.redis.dto.HospitalInfoDto;
import com.hc.my.common.core.util.ElkLogDetailUtil;
import com.hc.po.Userright;
import com.hc.service.MessageSendService;
import com.hc.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 消息发送实现
 * @author hc
 */
@Service
@EnableBinding(EquipmentStateMsg.class)
public class MessageSendServiceImpl implements MessageSendService {

    @Autowired
    private EquipmentStateMsg socketMessage;


    @Autowired
    private MessageApi messageApi;

    @Override
    public boolean send(String message) {
        Message<String> build = MessageBuilder.withPayload(message).build();
         return  socketMessage.getOutputChannel().send(build);
    }

}
