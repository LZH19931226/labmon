package com.hc.service.serviceImpl;

import com.hc.exchange.SocketMessage;
import com.hc.service.MessagePushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@EnableBinding(SocketMessage.class)
public class MessagePushServiceImpl implements MessagePushService {


    @Autowired
    private SocketMessage socketMessage;

    @Override
    public boolean pushMessage(String message) {
        Message<String> build = MessageBuilder.withPayload(message).build();
        return socketMessage.getOutputChannel().send(build);
    }


}
