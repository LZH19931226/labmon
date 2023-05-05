package com.hc.controller;


import com.hc.listenter.SocketMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class MswkController {

    @Autowired
    private  SocketMessageListener  socketMessageListener;

    //测试mswk订阅逻辑,需要关闭监听器里面监听队列,关闭定时器timer
    @GetMapping("testMswk")
    public void testMswk(String messageContent){
        socketMessageListener.mswkMessage(messageContent,"1");
    }
}
