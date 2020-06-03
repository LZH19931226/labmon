package com.hc.serviceimpl;

import com.hc.exchange.AlmMsg;
import com.hc.service.AlmMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@EnableBinding(AlmMsg.class)
public class AlmMsgServiceImpl implements AlmMsgService {

    @Autowired
    private AlmMsg almMsg;



    @Override
    public boolean pushMessage(String message) {
        Message<String> build = MessageBuilder.withPayload(message).build();

        return almMsg.getOutputChannel().send(build);
    }

    @Override
    public boolean pushMessage1(String message) {
        Message<String> build = MessageBuilder.withPayload(message).build();

        return almMsg.getOutputChannel1().send(build);
    }
}
