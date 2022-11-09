package com.hc.service.serviceImpl;

import com.hc.my.common.core.redis.dto.ParamaterModel;
import com.hc.my.common.core.util.Crc16Utils;
import com.hc.my.common.core.util.SoundLightUtils;
import com.hc.service.MTOnlineBeanService;
import com.hc.tcp.TcpClientApi;
import com.hc.util.*;
import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class MTOnlineBeanServiceImpl implements MTOnlineBeanService {

    private static final Logger log = LoggerFactory.getLogger(MTOnlineBeanServiceImpl.class);

    public static List<String> arrListt = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "0");

    @Autowired
    private NettyUtil netty;

    @Autowired
    private TcpClientApi tcpClientApi;


    @Override
    public void  sendMsg(String sn, String message) {
        String channelId = tcpClientApi.getChannelId(sn).getResult();
        if (StringUtils.isBlank(channelId)) {
            return;
        }
        Channel channel = netty.getChannelByTid(channelId);
        if(ObjectUtils.isEmpty(channel)){
          log.error(SoundLightUtils.PIPE_INFORMATION_NOT_FOUND+"sn为："+sn);
          return;
        }
        netty.sendData(channel, message);
        log.info("向该通道" + channel.id().asShortText() + "发送的内容:" + message);
    }

    @Override
    public List<ParamaterModel> paseData(String data) {
        // 数据拆分 4843 开头后 加上异或长度 最后两位是否是23
        List<String> ruleone = MathUtil.ruleone(data);
        if (CollectionUtils.isEmpty(ruleone)) {
            return null;
        }
        // 异或校验
        List<String> ruleTwo = MathUtil.ruleTwo(ruleone);
        if (CollectionUtils.isEmpty(ruleTwo)) {
            return null;
        }
        List<ParamaterModel> list = new ArrayList<>();
        // sn号 命令id 值 获取
        for (String cmd : ruleTwo) {
            // 获取sn号
            ParamaterModel paramaterModel = new ParamaterModel();
            String substring = cmd.substring(8, 28);
            String sn = HexStringUtils.fromHex(substring);
            if (!arrListt.contains(sn.substring(0, 1))) {
                log.error("不存在该sn" + sn);
                continue;
            }
            // 获取命令id
            String cmdid = cmd.substring(4, 6);
            String substring1 = cmdid.substring(0, 1);
            //CRC16校验 旧协议不需要,旧协议有7,8,9,A
            if (!StringUtils.equalsAnyIgnoreCase(substring1,"7","8","9","a")){
                if(!Crc16Utils.getCRCCodeIsTrue(data)){
                    continue;
                }
            }
            paramaterModel.setSN(sn);
            paramaterModel.setCmdid(cmdid);
        }
        return list;
    }



}
