//package com.hc.listenter;
//
//import com.hc.exchange.SocketMessageInput;
//import com.hc.utils.NettyUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.stream.annotation.EnableBinding;
//import org.springframework.cloud.stream.annotation.StreamListener;
//import org.springframework.stereotype.Component;
//
//@Component
//@EnableBinding(SocketMessageInput.class)
//public class TcpMessageLinstener {
//    private static final Logger LOGGER = LoggerFactory.getLogger(TcpMessageLinstener.class);
//
//
//    @Value("${delphi.ip}")
//    private  String ip;
//
//
//    @Value("${delphi.port}")
//    private  String port;
//
//
//
//    //监听报警信息
//    @StreamListener(SocketMessageInput.EXCHANGE_NAME4)
//    public void onMessage1(byte[] arr) {
//        LOGGER.info("从通道:"+SocketMessageInput.EXCHANGE_NAME4+"获得得数组:"+arr);
//             try {
//            NettyUtil.SocketSend(ip,port,arr);
//                  }catch (Exception e){
//                 LOGGER.error(SocketMessageInput.EXCHANGE_NAME4+"通道异常数据"+arr);
//                  return;
//                    }
//    }
//    //监听报警信息
//    @StreamListener(SocketMessageInput.EXCHANGE_NAME5)
//    public void onMessage2(byte[] arr) {
//        LOGGER.info("从通道:"+SocketMessageInput.EXCHANGE_NAME5+"获得得数组:"+arr);
//        try {
//            NettyUtil.SocketSend(ip,port,arr);
//        }catch (Exception e){
//            LOGGER.error(SocketMessageInput.EXCHANGE_NAME5+"通道异常数据"+arr);
//            return;
//        }
//    }
//    //监听报警信息
//    @StreamListener(SocketMessageInput.EXCHANGE_NAME6)
//    public void onMessage3(byte[] arr) {
//        LOGGER.info("从通道:"+SocketMessageInput.EXCHANGE_NAME6+"获得得数组:"+arr);
//        try {
//            NettyUtil.SocketSend(ip,port,arr);
//        }catch (Exception e){
//            LOGGER.error(SocketMessageInput.EXCHANGE_NAME6+"通道异常数据"+arr);
//            return;
//        }
//    }
//
//}
