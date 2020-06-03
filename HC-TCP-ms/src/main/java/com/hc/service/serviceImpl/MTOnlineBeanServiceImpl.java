package com.hc.service.serviceImpl;

import com.hc.bean.ApiResponse;
import com.hc.bean.MTOnlineBean;
import com.hc.bean.ParamaterModel;
import com.hc.config.RedisTemplateUtil;
import com.hc.service.MTOnlineBeanService;
import com.hc.socketServer.IotServer;
import com.hc.util.*;
import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.hc.util.cmdidParseUtils.paseAir;
import static com.hc.util.cmdidParseUtils.pasetemperature;
import static com.hc.util.paramaterModelUtils.electricity;

@Service
public class MTOnlineBeanServiceImpl implements MTOnlineBeanService {

    private static final Logger log = LoggerFactory.getLogger(MTOnlineBeanServiceImpl.class);

    public static List<String> arrListt = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "0");

    @Autowired
    private NettyUtil netty;

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;


    @Override
    public ApiResponse<String> sendMsg(String MId, String cmd) {
        try {
            Channel channel = netty.getChannelByTid(MId);
            log.info("该SN号绑定的通道id:" + channel.id().asShortText());
            netty.sendData(channel, cmd);
            log.info("向该通道" + channel.id().asShortText() + "发送的内容:" + cmd);
        } catch (Exception e) {
            log.error("该通道发送指令失败" + e);
            return ApiResponse.fail("发送失败" + e);

        }
        return ApiResponse.success();
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
            paramaterModel.setSN(sn);
            paramaterModel.setCmdid(cmdid);
            log.info("SN:" + sn+"  cmdid:"+cmdid);
            switch (cmdid) {
                // 心跳包
                case "88":
                    list.add(paramaterModel);
                    continue;
                    // 温度电量
                case "85":
                    ParamaterModel paramaterModel2 = cmdidParseUtils.Paser85(cmd, sn, cmdid);
                    list.add(paramaterModel2);
                    continue;
                    // CO2 O2 二氧化碳氧气
                case "87":
                    ParamaterModel paramaterModel3 = cmdidParseUtils.Paser87(cmd, sn, cmdid);
                    list.add(paramaterModel3);
                    continue;
                    // 市电
                case "89":
                    String substring3 = cmd.substring(28, 30);
                    String gas3 = electricity(substring3);
                    paramaterModel.setUPS(gas3);
                    list.add(paramaterModel);
                    continue;
                    // 开关门记录
                case "8d":
                    String substring5 = cmd.substring(28, 30);
                    String gas4 = electricity(substring5);
                    paramaterModel.setDOOR(gas4);
                    String substring6 = cmd.substring(30, 32);
                    String gas5 = electricity(substring6);
                    paramaterModel.setDOORZ(gas5);
                    list.add(paramaterModel);
                    continue;
                case "8e":
                    // 湿度
                    String substring7 = cmd.substring(28, 32);
                    String gas6 = paramaterModelUtils.gas(substring7);
                    gas6 = CustomUtils.agreementAll(gas6, "0", "100");
                    paramaterModel.setRH(gas6);
                    // 压力
                    String substring8 = cmd.substring(32, 36);
                    String electricity2 = electricity(substring8);
                    electricity2 = CustomUtils.agreementAll(electricity2, "600", "1200");
                    paramaterModel.setPRESS(electricity2);
                    // PM2.5
                    String substring9 = cmd.substring(36, 40);
                    String electricity3 = electricity(substring9);
                    electricity3 = CustomUtils.agreementAll(electricity3, "0", "500");
                    paramaterModel.setPM25(electricity3);
                    // PM10
                    String substring10 = cmd.substring(40, 44);
                    String electricity4 = electricity(substring10);
                    electricity4 = CustomUtils.agreementAll(electricity4, "0", "500");
                    paramaterModel.setPM10(electricity4);
                    // VOC
                    String substring11 = cmd.substring(44, 48);
                    String gas7 = paramaterModelUtils.gas(substring11);
                    gas7 = CustomUtils.agreementAll(gas7, "0", "200");
                    paramaterModel.setVOC(gas7);
                    // 甲醛
                    String substring12 = cmd.substring(48, 52);
                    String electricity5 = paramaterModelUtils.electricity2(substring12);
                    electricity5 = CustomUtils.agreementAll(electricity5,"0","2");
                    paramaterModel.setOX(electricity5);
                    list.add(paramaterModel);
                    continue;
                    //模拟500的温度
                case "70":
                    String wendu = cmd.substring(28, 32);
                    String pasetemperature = pasetemperature(wendu);
                    if (StringUtils.equals("-1.0",pasetemperature)) {
                        pasetemperature = "A";
                    }
                    paramaterModel.setTEMP(pasetemperature);
                    list.add(paramaterModel);
                    continue;
                    //模拟500的co2
                case "71":
                    String eryang = cmd.substring(28, 32);
                    String s = paseAir(eryang);
                    paramaterModel.setCO2(s);
                    list.add(paramaterModel);
                    continue;
                    //模拟500的RH
                case "72":
                    String rh = cmd.substring(28, 32);
                    String gas8 = paramaterModelUtils.gas(rh);

                    paramaterModel.setRH(gas8);
                    list.add(paramaterModel);
                    continue;
                    //模拟500的VOC
                case "73":
                    String voc = cmd.substring(28, 32);
                    String gas9 = paramaterModelUtils.gas(voc);
                    paramaterModel.setVOC(gas9);
                    list.add(paramaterModel);
                    continue;
                    //模拟500 PM2.5
                case "74":
                    String pm25 = cmd.substring(28, 32);
                    String electricity1 = paramaterModelUtils.gas(pm25);
                    paramaterModel.setPM25(electricity1);
                    list.add(paramaterModel);
                    continue;
                    //模拟500 O2
                case "75":
                    String O2 = cmd.substring(28, 32);
                    String s1 = paseAir(O2);
                    paramaterModel.setO2(s1);
                    list.add(paramaterModel);
                    continue;
                    //模拟500 QC
                case "76":
                    String qc = cmd.substring(28, 32);
                    String electricity6 = paramaterModelUtils.gas(qc);
                    paramaterModel.setQC(electricity6);
                    list.add(paramaterModel);
                    continue;
                    //模拟500 UPS
                case "77":
                    String ups = cmd.substring(28, 32);
                    String electricity7 = paramaterModelUtils.gas(ups);
                    paramaterModel.setUPS(electricity7);
                    list.add(paramaterModel);
                    continue;
                    //模拟500 door
                case "78":
                    String door = cmd.substring(28, 32);
                    String electricity8 = paramaterModelUtils.gas(door);
                    paramaterModel.setDOOR(electricity8);
                    list.add(paramaterModel);
                    continue;
                    //模拟500 OX
                case "79":
                    String jiaquan = cmd.substring(28, 32);
                    String electricity9 = paramaterModelUtils.gas(jiaquan);
                    paramaterModel.setOX(electricity9);
                    list.add(paramaterModel);
                    continue;
                case "7a":
                    String pm10 = cmd.substring(28, 32);
                    String electricity10 = paramaterModelUtils.gas(pm10);
                    paramaterModel.setPM10(electricity10);
                    list.add(paramaterModel);
                    continue;
                case "7b":
                    String qiliu = cmd.substring(28, 32);
                    String electricity11 = paramaterModelUtils.gas(qiliu);
                    paramaterModel.setPRESS(electricity11);
                    list.add(paramaterModel);
                    continue;
                case "91":
                    ParamaterModel paramaterModel91 = cmdidParseUtils.pase91(cmd);
                    paramaterModel91.setSN(sn);
                    paramaterModel91.setCmdid(cmdid);
                    list.add(paramaterModel91);
                    continue;
                case "90":
                    ParamaterModel paramaterModel90 = cmdidParseUtils.pase90(cmd);
                    paramaterModel90.setSN(sn);
                    paramaterModel90.setCmdid(cmdid);
                    list.add(paramaterModel90);
                    continue;
                case "92":
                    ParamaterModel paramaterModel92 = cmdidParseUtils.pase92(cmd);
                    paramaterModel92.setSN(sn);
                    paramaterModel92.setCmdid(cmdid);
                    list.add(paramaterModel92);
                    continue;
                case "93":
                    ParamaterModel paramaterModel93 = cmdidParseUtils.pase93(cmd);
                    paramaterModel93.setSN(sn);
                    paramaterModel93.setCmdid(cmdid);
                    list.add(paramaterModel93);
                    continue;
                case "94":
                    ParamaterModel paramaterModel94 = cmdidParseUtils.pase94(cmd);
                    paramaterModel94.setSN(sn);
                    paramaterModel94.setCmdid(cmdid);
                    list.add(paramaterModel94);
                    continue;
                case "95":
                    log.info("收到声光报警开启应答成功:" + sn);
                    redisTemplateUtil.boundValueOps(sn + "lgtalarm").set("1");
                    continue;
                case "96":
                    log.info("收到声光报警关闭应答成功:" + sn);
                    redisTemplateUtil.boundValueOps(sn + "lgtalarm").set("2");
                    continue;
                case "97":
                    ParamaterModel paramaterModel1 = cmdidParseUtils.pase97(cmd);
                    paramaterModel1.setSN(sn);
                    paramaterModel1.setCmdid(cmdid);
                    list.add(paramaterModel1);
                    continue;
                case "8a":
                    ParamaterModel paramaterModel4 = cmdidParseUtils.pase8a(cmd);
                    paramaterModel4.setSN(sn);
                    paramaterModel4.setCmdid(cmdid);
                    list.add(paramaterModel4);
                    continue;
                case "8b":
                    ParamaterModel paramaterModel5 = cmdidParseUtils.pase8b(cmd);
                    paramaterModel5.setSN(sn);
                    paramaterModel5.setCmdid(cmdid);
                    list.add(paramaterModel5);
                    continue;
                case "8c":
                    ParamaterModel paramaterModel6 = cmdidParseUtils.pase8c(cmd);
                    paramaterModel6.setSN(sn);
                    paramaterModel6.setCmdid(cmdid);
                    list.add(paramaterModel6);
                    continue;
                case "98":
                    ParamaterModel paramaterModel7 = cmdidParseUtils.pase98(cmd);
                    paramaterModel7.setSN(sn);
                    paramaterModel7.setCmdid(cmdid);
                    list.add(paramaterModel7);
                    continue;
                case "99":
                    ParamaterModel paramaterModel8 = cmdidParseUtils.pase99(cmd);
                    paramaterModel8.setSN(sn);
                    paramaterModel8.setCmdid(cmdid);
                    list.add(paramaterModel8);
                    continue;
                case "9a":
                    ParamaterModel paramaterModel9 = cmdidParseUtils.pase9A(cmd);
                    paramaterModel9.setSN(sn);
                    paramaterModel9.setCmdid(cmdid);
                    list.add(paramaterModel9);
                    continue;
                case "9b":
                    ParamaterModel paramaterModel10 = cmdidParseUtils.pase9b(cmd, sn, cmdid);
                    list.add(paramaterModel10);
                    continue;
                case "9c":
                    ParamaterModel paramaterModel11 = cmdidParseUtils.pase9c(cmd, sn, cmdid);
                    list.add(paramaterModel11);
                    continue;
                case "9f":
                    ParamaterModel paramaterModel12 = cmdidParseUtils.pase9f(cmd, sn, cmdid);
                    list.add(paramaterModel12);
                    continue;
                case "a1":
                    ParamaterModel paramaterModel13 = cmdidParseUtils.paseA1(cmd, sn, cmdid);
                    list.add(paramaterModel13);
                    continue;
                case "a2":
                    ParamaterModel paramaterModel14 = cmdidParseUtils.paseA2(cmd, sn, cmdid);
                    list.add(paramaterModel14);
                    continue;
            }

        }

        return list;
    }


    @Override
    public List<MTOnlineBean> getall() {
        List<MTOnlineBean> list = new ArrayList<>();
        Set<Channel> sets = IotServer.onlineChannels;
        for (Channel channel : sets) {
            MTOnlineBean mtOnlineBean = new MTOnlineBean();
            String channelId = channel.id().asShortText();
            mtOnlineBean.setCid(channelId);
            String snByCid = netty.getSnByCid(channelId);
            mtOnlineBean.setTid(snByCid);
            list.add(mtOnlineBean);
        }
        return list;
    }

    public static void main(String args[]) {
        for (int i = 0; i < 100; i++) {
            System.out.println(37.01 + 0.01);
        }
    }


}
