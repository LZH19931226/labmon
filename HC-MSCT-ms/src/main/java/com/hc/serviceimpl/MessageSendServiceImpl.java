package com.hc.serviceimpl;

import com.hc.exchange.EquipmentStateMsg;
import com.hc.service.MessageSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * 消息发送实现
 * @author hc
 */
@Service
@EnableBinding(EquipmentStateMsg.class)
public class MessageSendServiceImpl implements MessageSendService {

    @Autowired
    private EquipmentStateMsg socketMessage;

    @Override
    public boolean send(String message) {
        Message<String> build = MessageBuilder.withPayload(message).build();
         return  socketMessage.getOutputChannel().send(build);
    }
}
