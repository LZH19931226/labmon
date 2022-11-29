package com.hc.mqttsend;

import cn.hutool.json.JSONUtil;
import com.hc.my.common.core.redis.dto.HarvesterDto;
import com.hc.my.common.core.util.DateUtils;
import com.hc.my.common.core.util.HexUtils;
import com.hc.service.MessagePushService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * mqtt 推送and接收 消息类
 **/
@Configuration
@IntegrationComponentScan
@Slf4j
public class MqttSenderAndReceiveConfig {


    @Value("${mqtt.username}")
    private String username;

    @Value("${mqtt.password}")
    private String password;

    @Value("${mqtt.url}")
    private String hostUrl;

    @Value("${mqtt.client.id}")
    private String clientId;

    @Value("${mqtt.default.topic}")
    private String defaultTopic;

    @Value("${mqtt.completionTimeout}")
    private int completionTimeout;   //连接超时

    @Autowired
    private  MqttGateway mqttGateway;

    @Autowired
    private MessagePushService messagePushService;
    /**
     * MQTT连接器选项
     **/
    @Bean(value = "getMqttConnectOptions")
    public MqttConnectOptions getMqttConnectOptions1() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
        mqttConnectOptions.setCleanSession(true);
        // 设置超时时间 单位为秒
        mqttConnectOptions.setConnectionTimeout(10);
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());
        mqttConnectOptions.setServerURIs(new String[]{hostUrl});
        // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送心跳判断客户端是否在线，但这个方法并没有重连的机制
        mqttConnectOptions.setKeepAliveInterval(10);
        // 设置“遗嘱”消息的话题，若客户端与服务器之间的连接意外中断，服务器将发布客户端的“遗嘱”消息。
        //mqttConnectOptions.setWill("willTopic", WILL_DATA, 2, false);
        return mqttConnectOptions;
    }

    /**
     * MQTT工厂
     **/
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(getMqttConnectOptions1());
        return factory;
    }

    /**
     * MQTT信息通道（生产者）
     **/
    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    /**
     * MQTT消息处理器（生产者）
     **/
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(clientId + "_producer", mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(defaultTopic);
        messageHandler.setAsyncEvents(true); // 消息发送和传输完成会有异步的通知回调
        //设置转换器 发送bytes数据
        DefaultPahoMessageConverter converter = new DefaultPahoMessageConverter();
        converter.setPayloadAsBytes(true);
        return messageHandler;
    }

    /**
     * 配置client,监听的topic
     * MQTT消息订阅绑定（消费者）
     **/
    @Bean
    public MessageProducer inbound() {
        List<String> topicList = Arrays.asList(defaultTopic.trim().split(","));
        String[] topics = new String[topicList.size()];
        topicList.toArray(topics);
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(clientId + "_consumer", mqttClientFactory(), topics);
        adapter.setCompletionTimeout(completionTimeout);
        DefaultPahoMessageConverter converter = new DefaultPahoMessageConverter();
        converter.setPayloadAsBytes(true);
        adapter.setConverter(converter);
        adapter.setQos(2);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    /**
     * MQTT信息通道（消费者）
     **/
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    /**
     * MQTT消息处理器（消费者）
     **/
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return message -> {
            //处理接收消息
            String topic = (String) message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC);
            byte[] payLoad = (byte[]) message.getPayload();
            String cmdData = HexUtils.bytesToHexString(payLoad);
            log.info("topic:{},收到消息:{}",topic,cmdData);
            //指令为21需要应答心跳包信息
            //9E BF  Length 22 01 01 00 时间搓  CheckSum EE BA
            //9E BF 00 05 21 01 01 00 28 EE BA
            String typeID = cmdData.substring(8, 10);
            String cmdId = cmdData.substring(10, 12);
            if (StringUtils.equals(typeID, "21")&&StringUtils.equals(cmdId, "01")) {
                //心跳应答
                replyHeart(topic);
            }
            //采集器
            if (StringUtils.equals(typeID, "21")&&StringUtils.equals(cmdId, "02")) {
                //BE0FEBM0N0/BE0FEBM0N00083002001/user/update
                //9EBF00182102010005808A4504F28291636DB35800E2BA14014064C9EEBA
                harvester(cmdData);
            }
            //中继器
            if (StringUtils.equals(typeID, "21")&&StringUtils.equals(cmdId, "03")) {
                //BE0FEBM0N1/BE0FEBM0N10092310085/user/update
                //9EBF00122103010005808A45636DB3A8010001A41E7AEEBA
                repeaters(cmdData);
            }


        };
    }

    private void repeaters(String cmddata) {
        HarvesterDto harvester  = new HarvesterDto();
        harvester.setId(DateUtils.getCurrentYYMM());
        harvester.setCreatTime(new Date());
        String zsn  =cmddata.substring(16,24);
        harvester.setRepeatersSn(HexUtils.hexToInt(zsn));
        String time =  cmddata.substring(24,32);
        harvester.setTimeStamp(HexUtils.hexToInt(time));
        String gd = cmddata.substring(32, 34);
        String s = HexUtils.hexToInt(gd);
        if (StringUtils.equals(s,"1")){
            harvester.setPowerSupplyStatus("电池供电");
        }else{
            harvester.setPowerSupplyStatus("正常电源供电");
        }
        String s1 = HexUtils.hexToInt(cmddata.substring(34, 36));
        if (StringUtils.equals(s1,"0")){
            harvester.setSdCard("正常");
        }else if(StringUtils.equals(s1,"1")){
            harvester.setSdCard("未检测到");
        }else {
            harvester.setSdCard("异常");
        }
        String qc = cmddata.substring(36,40);
        harvester.setCellVoltage(HexUtils.hexToInt(qc,100.00));
        String xin = cmddata.substring(40,42);
        harvester.setSignalIntensity(HexUtils.hexToInt(xin));
        messagePushService.pushMessage(JSONUtil.toJsonStr(harvester));
        log.info("中继器解析出来的数据模型:{}",harvester);
    }

    private void harvester(String cmddata) {
        HarvesterDto harvester  = new HarvesterDto();
        harvester.setCreatTime(new Date());
        harvester.setId(DateUtils.getCurrentYYMM());
        String zsn  =cmddata.substring(16,24);
        harvester.setRepeatersSn(HexUtils.hexToInt(zsn));
        String csn  = cmddata.substring(24,32);
        harvester.setHarvesterSn(HexUtils.hexToInt(csn));
        String time =  cmddata.substring(32,40);
        harvester.setTimeStamp(HexUtils.hexToInt(time));
        String yewei= cmddata.substring(40,44);
        harvester.setLiquidLevel(HexUtils.hexToInt(yewei)+"mm");
        String temp = cmddata.substring(44,48);
        int i = HexUtils.hexTempToString(temp);
        String temp1 = String.valueOf(i);
        if (temp1.equals("32767")){
            harvester.setTemp("温度传感器未接");
        }else if (temp1.equals("32766")){
            harvester.setTemp("温度传感器故障");
        }else {
            harvester.setTemp(String.valueOf(i/100));
        }
        String qc = cmddata.substring(48,52);
        harvester.setCellVoltage(HexUtils.hexToInt(qc,100.00));
        String xin = cmddata.substring(52,54);
        harvester.setSignalIntensity(HexUtils.hexToInt(xin));
        messagePushService.pushMessage(JSONUtil.toJsonStr(harvester));
        log.info("采集器解析出来的数据模型:{}",harvester);
    }


    public void replyHeart(String topic){
        //时间搓固定为4位
        String time = HexUtils.getHexCurrentTimeUnix4byte();
        //9F BF 00 09 22 01 01 00 63 69 BB 51 XX EE BA
        //00 09 22 01 01 00 63 69 BB 51 每一位都需要转换10进制相加 再转换为16进制得出CheckSum
        String s1 = HexUtils.hexToInt("00");
        String s2 = HexUtils.hexToInt("09");
        String s3 = HexUtils.hexToInt("22");
        String s4 = HexUtils.hexToInt("01");
        String s5 = HexUtils.hexToInt("01");
        String s6 = HexUtils.hexToInt("00");
        String substring1 = time.substring(0, 2);
        String substring2 = time.substring(2, 4);
        String substring3 = time.substring(4, 6);
        String substring4 = time.substring(6, 8);
        String s7 = HexUtils.hexToInt(substring1);
        String s8 = HexUtils.hexToInt(substring2);
        String s9 = HexUtils.hexToInt(substring3);
        String s10 = HexUtils.hexToInt(substring4);
        long CheckSumIntValue = Integer.parseInt(s1) + Integer.parseInt(s2) + Integer.parseInt(s3) + Integer.parseInt(s4)
                + Integer.parseInt(s5) + Integer.parseInt(s6) + Integer.parseInt(s7)
                + Integer.parseInt(s8) + Integer.parseInt(s9) + Integer.parseInt(s10);
        String CheckSumStringValue = HexUtils.intToHex(CheckSumIntValue % 256);
        if (CheckSumStringValue.length() == 1) {
            CheckSumStringValue = "0" + CheckSumStringValue;
        }
        //应答字符串最终格式
        String replyData = "9EBF000922010100"+time+CheckSumStringValue+"EEBA";
        //应答将update变成send主题
        String newTopic = topic.replace("update", "get");
        mqttGateway.sendToMqtt(newTopic,HexUtils.hexStringToBytes(replyData));
        log.info("topic:{},应答消息:{}",newTopic,replyData);
    }

}
