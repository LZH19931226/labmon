//package com.hc.config;
//
//import org.apache.catalina.connector.Connector;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
///**
// * Created by 15350 on 2020/2/20.
// */
//@Component
//public class HttpsConfig {
//
//
//    @Bean
//    @ConditionalOnExpression("'${spring.profiles.active}'.equals('pro')")
//    public Connector connector(){
//        Connector connector=new Connector("org.apache.coyote.http11.Http11NioProtocol");
//        connector.setScheme("http");
//        connector.setPort(8093);
//        connector.setSecure(false);
//        connector.setRedirectPort(8093);
//        return connector;
//    }
//
//
//
//}
//
//
