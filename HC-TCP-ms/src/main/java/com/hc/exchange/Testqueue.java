package com.hc.exchange;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Testqueue {

    public final static String TESTQUEUE = "testqueues";

//    @RabbitListener(queuesToDeclare = @Queue(TESTQUEUE))
//    public void process(String message){
//         log.info("111:{}",message);
//    }
}
