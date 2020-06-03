package com.hc.serviceImpl;

import com.hc.exchange.BaoJinMsg;
import com.hc.service.MessagePushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@EnableBinding(BaoJinMsg.class)
public class MessagePushServiceImpl implements MessagePushService {

    @Autowired
    private BaoJinMsg socketMessage;


    @Override
    public boolean pushMessage1(String message) {
        Message<String> build = MessageBuilder.withPayload(message).build();

        return socketMessage.getOutputChannel1().send(build);
    }

    @Override
    public boolean pushMessage2(String message) {
        Message<String> build = MessageBuilder.withPayload(message).build();

        return socketMessage.getOutputChannel2().send(build);
    }

    @Override
    public boolean pushMessage3(String message) {
        Message<String> build = MessageBuilder.withPayload(message).build();

        return socketMessage.getOutputChannel3().send(build);
    }

    @Override
    public boolean pushMessage4(String message) {
        Message<String> build = MessageBuilder.withPayload(message).build();
        return socketMessage.getOutputChannel4().send(build);

    }

    @Override
    public boolean pushMessage5(String message) {
        Message<String> build = MessageBuilder.withPayload(message).build();
        return socketMessage.getOutputChannel5().send(build);
    }


}
