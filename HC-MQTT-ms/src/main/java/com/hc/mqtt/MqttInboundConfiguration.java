package com.hc.mqtt;//package com.hc.haier.mqtt;
//
//
//import com.hc.haier.util.HexUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.integration.annotation.IntegrationComponentScan;
//import org.springframework.integration.annotation.ServiceActivator;
//import org.springframework.integration.channel.DirectChannel;
//import org.springframework.integration.core.MessageProducer;
//import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
//import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
//import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
//import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
//import org.springframework.integration.mqtt.support.MqttHeaders;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.MessageHandler;
//
//
///**
// * MQTT消费端
// *
// */
//@Configuration
//@IntegrationComponentScan
//public class MqttInboundConfiguration {
//
//    @Autowired
//    private MqttConfiguration mqttProperties;
//
//    @Bean
//    public MessageChannel mqttInputChannel() {
//        return new DirectChannel();
//    }
//
//    @Bean
//    public MqttPahoClientFactory mqttClientFactory() {
//        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
//        String[] array = mqttProperties.getUrl().split(",");
//        MqttConnectOptions options = new MqttConnectOptions();
//        options.setServerURIs(array);
//        options.setUserName(mqttProperties.getUsername());
//        options.setPassword(mqttProperties.getPassword().toCharArray());
//        options.setKeepAliveInterval(2);
//
//        //接受离线消息
//        options.setCleanSession(false);
//        factory.setConnectionOptions(options);
//        return factory;
//    }
//
//    //配置client,监听的topic
//    @Bean
//    public MessageProducer inbound() {
//        String[] inboundTopics = mqttProperties.getTopics().split(",");
//        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
//                mqttProperties.getClientId()+"_inbound",mqttClientFactory(), inboundTopics);  //对inboundTopics主题进行监听
//        adapter.setCompletionTimeout(5000);
//        adapter.setQos(1);
//        DefaultPahoMessageConverter converter = new DefaultPahoMessageConverter();
//        converter.setPayloadAsBytes(true);
//        adapter.setConverter(converter);
//        adapter.setOutputChannel(mqttInputChannel());
//        return adapter;
//    }
//
//
//
//
//
//    //通过通道获取数据
//    @Bean
//    @ServiceActivator(inputChannel = "mqttInputChannel")  //异步处理
//    public MessageHandler handler() {
//        return message -> {
//            String topic = (String) message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC);
//            System.out.println("topic:"+topic);
//            byte[] payLoad = (byte[])message.getPayload();
//            String cmdData = HexUtils.bytes2HexString(payLoad);
//            System.out.println("message:"+cmdData);
//            //指令为21需要应答心跳包信息
//            //9E BF  Length 22 01 01 00 时间搓  CheckSum EE BA
//            //9E BF 00 05 21 01 01 00 28 EE BA
//            String typeID = cmdData.substring(10, 12);
//            if (StringUtils.equals(typeID,"21")){
//                long l = System.currentTimeMillis();
//                //时间搓固定为4位
//                String time = HexUtils.intToHex(l);
//                //9F BF 00 09 22 01 01 00 63 69 BB 51 XX EE BA
//                //00 09 22 01 01 00 63 69 BB 51 每一位都需要转换10进制相加 再转换为16进制得出CheckSum
//                String s1 = HexUtils.hexToInt("00");
//                String s2 = HexUtils.hexToInt("09");
//                String s3 = HexUtils.hexToInt("22");
//                String s4 = HexUtils.hexToInt("01");
//                String s5 = HexUtils.hexToInt("01");
//                String s6 = HexUtils.hexToInt("00");
//                String substring1 = time.substring(0, 2);
//                String substring2 = time.substring(2, 4);
//                String substring3 = time.substring(4, 6);
//                String substring4 = time.substring(6, 8);
//                String s7 = HexUtils.hexToInt(substring1);
//                String s8 = HexUtils.hexToInt(substring2);
//                String s9 = HexUtils.hexToInt(substring3);
//                String s10 = HexUtils.hexToInt(substring4);
//                long   CheckSumIntValue =   Integer.parseInt(s1)+Integer.parseInt(s2)+Integer.parseInt(s3)+Integer.parseInt(s4)
//                      +Integer.parseInt(s5)+Integer.parseInt(s6)+Integer.parseInt(s7)
//                      +Integer.parseInt(s8)+Integer.parseInt(s9)+Integer.parseInt(s10);
//                String CheckSumStringValue = HexUtils.intToHex(CheckSumIntValue % 256);
//                if (CheckSumStringValue.length()==1){
//                    CheckSumStringValue="0"+CheckSumStringValue;
//                }
//                System.out.println("转换的十六进制的数为："+CheckSumStringValue);
//                //应答字符串最终格式
//                String replyData= "9EBF000922010100"+time+CheckSumStringValue+"EEBA";
//                String sendTopic= "/BE0FEBM0N1/BE0FEBM0N10092310085/user/get";
//
//
//
//
//            }
//
//
//
//
//
//
//        };
//    }
//}
