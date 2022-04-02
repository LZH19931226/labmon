package com.hc.controller;

import com.hc.provider.WebSocketSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author LiuZhiHao
 * @date 2021/5/28 10:09
 * 描述:
 **/

@RestController
@RequestMapping("/socket")
@Slf4j
public class MsgController {

    @Autowired
    private ConcurrentHashMap<String, WebSocketSender> senderMap;

    @RequestMapping("/send")
    public void sendMessage(@RequestParam String id, @RequestParam String data) {
        WebSocketSender sender = senderMap.get(id);
        if (sender != null) {
            sender.sendData(data);
            log.info(String.format("Message '%s' sent to connection: %s.", data, id));
        } else {
            log.info(String.format("Connection of id '%s' doesn't exist", id));
        }
    }
}
