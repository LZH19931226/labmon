package com.hc.mqttsend;//package com.hc.haier.mqttsend;
//
//import com.alibaba.fastjson.JSON;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.integration.mqtt.support.MqttHeaders;
//import org.springframework.messaging.Message;
//import org.springframework.stereotype.Component;
//
///**
// * mqtt客户端消息处理类
// **/
//@Slf4j
//@Component
//public class MqttReceiveHandle {
//
//    public void handle(Message<?> message) {
//        log.info("收到订阅消息: {}", message);
//        String topic = message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC).toString();
//        log.info("消息主题：{}", topic);
//        Object payLoad = message.getPayload();
//        byte[] data = (byte[]) payLoad;
//        Packet packet = Packet.parse(data);
//        log.info("发送的Packet数据{}", JSON.toJSONString(packet));
//
//    }
//}