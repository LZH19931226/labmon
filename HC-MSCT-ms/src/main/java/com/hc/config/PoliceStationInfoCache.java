//package com.hc.config;
//
//import com.hc.msn.ReceiveDemo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
///**
// * 启动msn队列服务
// */
//@Component
//@Order(value = 1)
//public class PoliceStationInfoCache implements CommandLineRunner {
//    @Autowired
//    private ReceiveDemo receiveDemo;
//    @Autowired
//    private RedisTemplateUtil redisTemplateUtil;
//    @Override
//    public void run(String... strings) throws Exception {
//        redisTemplateUtil.delete("phonenum:time");
//        System.out.println("MSN服务准备");
//        receiveDemo.run();
//        System.out.println("MSN服务已经启动");
//    }
//}
//
//
////