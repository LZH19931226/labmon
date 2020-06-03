//package com.hc.msn;
//
//import com.alicom.mns.tools.DefaultAlicomMessagePuller;
//import com.alicom.mns.tools.MessageListener;
//import com.aliyun.mns.model.Message;
//import com.google.gson.Gson;
//import com.hc.config.RedisTemplateUtil;
//import com.hc.dao.ReceiptrecordDao;
//import com.hc.entity.Receiptrecord;
//import com.hc.service.SendMesService;
//import com.hc.utils.TimeHelper;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.BoundHashOperations;
//import org.springframework.stereotype.Component;
//
//import java.text.ParseException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
///**
// * 只能用于接收云通信的消息，不能用于接收其他业务的消息
// * 短信上行消息接收demo
// */
//@Component
//public class ReceiveDemo {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiveDemo.class);
//
//    @Autowired
//    private ReceiptrecordDao receiptrecordDao;
//
//    @Autowired
//    private SendMesService sendMesService;
//
//    @Autowired
//    private RedisTemplateUtil redisTemplateUtil;
//
//    class MyMessageListener implements MessageListener {
//        private Gson gson = new Gson();
//
//        @Override
//        public boolean dealMessage(Message message) {
//
//            //消息的几个关键值
//            LOGGER.info("message handle: " + message.getReceiptHandle());
//            LOGGER.info("MSN返回值: " + message.getMessageBodyAsString());
//            LOGGER.info("message id: " + message.getMessageId());
//            LOGGER.info("message dequeue count:" + message.getDequeueCount());
//
//            try {
//                String content = "未知";
//                Map<String, Object> contentMap = gson.fromJson(message.getMessageBodyAsString(), HashMap.class);
//                String status_code = contentMap.get("status_code").toString();
//                String callee = contentMap.get("callee").toString();
//                String end_time = contentMap.get("end_time").toString();
//                String toll_type = contentMap.get("toll_type").toString();
//                switch (status_code){
//                    case "200003":
//                        content = "不接听和挂断";
//                        break;
//                    case "200005":
//                        content = "拉黑，飞行、关机";
//                        break;
//                    case "200000":
//                        content = "拨打成功，已接听";
//                        break;
//                }
//                Receiptrecord receiptrecord = new Receiptrecord();
//                receiptrecord.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
//                receiptrecord.setContent(content);
//                receiptrecord.setPhonenum(callee);
//                receiptrecord.setSendtime(end_time);
//                receiptrecord.setReceiptcode(status_code);
//                receiptrecord.setTolltype(toll_type);
//                receiptrecordDao.save(receiptrecord);
//                //TODO 根据文档中具体的消息格式进行消息体的解析
//                /**
//                 * 回拨业务处理
//                 */
//                if (StringUtils.equalsAny(status_code,"200003","200005")) {
//                    redisTemplateUtil.boundListOps(callee).rightPush(callee);
//                    // 当回拨次数小于4时候，回拨
//                    Long size = redisTemplateUtil.boundListOps(callee).size();
//                    BoundHashOperations<Object, Object, Object> objectObjectObjectBoundHashOperations = redisTemplateUtil.boundHashOps("phonenum:time");
//                    LOGGER.info("回拨手机号："+callee+"---"+"---当前回拨次数："+size);
//                    if ( size < 4) {
//                        String o = (String)objectObjectObjectBoundHashOperations.get(callee);
//                        //判断当前回拨
//                        if (size == 1) {
//                            //判断上一次回拨时间
//
//                            if (StringUtils.isEmpty(o)) {
//                                //为空表示未回拨过 -- 执行回拨代码服务
//                                sendMesService.receivePhone(callee);
//                                //记录当前回拨时间
//                                String currentDateTime = TimeHelper.getCurrentDate();
//                                objectObjectObjectBoundHashOperations.put(callee,currentDateTime);
//                            }else {
//                                //与当前时间进行判断
//                                double datePoorMinByString = TimeHelper.getDatePoorMinByString(o, TimeHelper.getCurrentDate());
//                                if (datePoorMinByString < 60) {
//                                    //不执行回拨,并清除缓存
//
//                                    redisTemplateUtil.delete(callee);
//                                }
//                            }
//                        }else {
//                            //  计算当前回拨时间
//                            double datePoorMinByString = TimeHelper.getDatePoorMinByString(o, TimeHelper.getCurrentDate());
//                            LOGGER.info("回拨时间间隔："+callee+"---"+datePoorMinByString + "---当前回拨次数："+size);
//                            if (datePoorMinByString < 10) {
//                                //不执行回拨,并清除当前增加的回拨次数（当前是一次）
//
//                                redisTemplateUtil.boundListOps(callee).remove(1,callee);
//                            }else {
//                                //执行回拨
//                                sendMesService.receivePhone(callee);
//                                //记录当前回拨时间
//                                String currentDateTime = TimeHelper.getCurrentDate();
//                                objectObjectObjectBoundHashOperations.put(callee,currentDateTime);
//                            }
//
//                        }
//                    }else {
//                        //第四次  清除回拨次数
//                        redisTemplateUtil.delete(callee);
//                    }
//                }else {
//                    redisTemplateUtil.delete(callee);
//                }
//
//                //TODO 这里开始编写您的业务代码
//
//            } catch (com.google.gson.JsonSyntaxException e) {
//                LOGGER.error("error_json_format:" + message.getMessageBodyAsString(), e);
//                //理论上不会出现格式错误的情况，所以遇见格式错误的消息，只能先delete,否则重新推送也会一直报错
//                return true;
//            } catch (Throwable e) {
//                //您自己的代码部分导致的异常，应该return false,这样消息不会被delete掉，而会根据策略进行重推
//                return false;
//            }
//
//            //消息处理成功，返回true, SDK将调用MNS的delete方法将消息从队列中删除掉
//            return true;
//        }
//
//    }
//
//    public void run() throws com.aliyuncs.exceptions.ClientException, ParseException {
//
//        DefaultAlicomMessagePuller puller = new DefaultAlicomMessagePuller();
//
//        //TODO 此处需要替换成开发者自己的AK信息
//        String accessKeyId = "LTAI4Fh7qr78heUy5w69wfSm";
//        String accessKeySecret = "hWikSbW0g2cyNoTCvNd6OEXBEFBy0p";
//
//		/*
//        * TODO 将messageType和queueName替换成您需要的消息类型名称和对应的队列名称
//		*云通信产品下所有的回执消息类型:
//		*1:短信回执：SmsReport，
//		*2:短息上行：SmsUp
//		*3:语音呼叫：VoiceReport
//		*4:流量直冲：FlowReport
//		*/
//        String messageType = "VoiceReport";//此处应该替换成相应产品的消息类型
//        String queueName = "Alicom-Queue-1559721194289684-VoiceReport";//在云通信页面开通相应业务消息后，就能在页面上获得对应的queueName,每一个消息类型
//        puller.startReceiveMsg(accessKeyId, accessKeySecret, messageType, queueName, new MyMessageListener());
//    }
//
//
//}
