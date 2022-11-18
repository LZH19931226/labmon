package com.hc.service.serviceImpl;

import com.hc.my.common.core.constant.enums.ProbeOutlier;
import com.hc.my.common.core.redis.dto.ParamaterModel;
import com.hc.service.MTOnlineBeanService;
import com.hc.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static com.hc.util.cmdidParseUtils.paseAir;
import static com.hc.util.cmdidParseUtils.pasetemperature;
import static com.hc.util.paramaterModelUtils.electricity;

@Service
public class MTOnlineBeanServiceImpl implements MTOnlineBeanService {

    @Override
    public List<ParamaterModel> paseData(String data) {
        // 数据拆分 4843 开头后 加上异或长度 最后两位是否是23
        List<String> ruleone = MathUtil.cutOutString4843(data);
        if (CollectionUtils.isEmpty(ruleone)) {
            return null;
        }

        List<ParamaterModel> list = new ArrayList<>();
        // sn号 命令id 值 获取
        for (String cmd : ruleone) {
            // 获取sn号
            ParamaterModel paramaterModel = new ParamaterModel();
            String sn = HexStringUtils.fromHex(cmd.substring(8, 28));
            String cmdid = cmd.substring(4, 6);
            paramaterModel.setSN(sn);
            paramaterModel.setCmdid(cmdid);
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
                    //48 43 8e 16 31393139313230303136 1572 03ef 0000 0001 00eb 005c aa23
                    // 湿度
                    String substring7 = cmd.substring(28, 32);
                    if (StringUtils.equalsAnyIgnoreCase(substring7, "ff00")) {
                        paramaterModel.setRH(ProbeOutlier.OUT_OF_TEST_RANGE.getCode());
                    } else {
                        String gas6 = paramaterModelUtils.gas(substring7);
                        gas6 = CustomUtils.agreementAll(gas6, "0", "100");
                        paramaterModel.setRH(gas6);
                    }
                    // 压力
                    String substring8 = cmd.substring(32, 36);
                    if (StringUtils.equalsAnyIgnoreCase(substring8, "ff00")) {
                        paramaterModel.setPRESS(ProbeOutlier.OUT_OF_TEST_RANGE.getCode());
                    } else {
                        String electricity2 = electricity(substring8);
                        electricity2 = CustomUtils.agreementAll(electricity2, "300", "1250");
                        paramaterModel.setPRESS(electricity2);
                    }
                    // PM2.5
                    String substring9 = cmd.substring(36, 40);
                    if (StringUtils.equalsAnyIgnoreCase(substring9, "ff00")) {
                        paramaterModel.setPM25(ProbeOutlier.OUT_OF_TEST_RANGE.getCode());
                    } else {
                        String electricity3 = electricity(substring9);
                        electricity3 = CustomUtils.agreementAll(electricity3, "0", "500");
                        paramaterModel.setPM25(electricity3);
                    }
                    // PM10
                    String substring10 = cmd.substring(40, 44);
                    if (StringUtils.equalsAnyIgnoreCase(substring10, "ff00")) {
                        paramaterModel.setPM10(ProbeOutlier.OUT_OF_TEST_RANGE.getCode());
                    } else {
                        String electricity4 = electricity(substring10);
                        electricity4 = CustomUtils.agreementAll(electricity4, "0", "500");
                        paramaterModel.setPM10(electricity4);
                    }
                    // VOC
                    String substring11 = cmd.substring(44, 48);
                    if (StringUtils.equalsAnyIgnoreCase(substring11, "9C00")) {
                        paramaterModel.setVOC(ProbeOutlier.OUT_OF_TEST_RANGE.getCode());
                    } else {
                        String gas7 = paramaterModelUtils.gas(substring11);
                        gas7 = CustomUtils.agreementAll(gas7, "0", "200");
                        paramaterModel.setVOC(gas7);
                    }
                    // 甲醛
                    String substring12 = cmd.substring(48, 52);
                    if (StringUtils.equalsAnyIgnoreCase(substring12, "ff00")) {
                        paramaterModel.setOX(ProbeOutlier.OUT_OF_TEST_RANGE.getCode());
                    } else {
                        String electricity5 = paramaterModelUtils.electricity2(substring12);
                        electricity5 = CustomUtils.agreementAll(electricity5, "0", "2");
                        paramaterModel.setOX(electricity5);
                    }
                    list.add(paramaterModel);
                    continue;
                    //模拟500的温度
                case "70":
                    String wendu = cmd.substring(28, 32);
                    String pasetemperature = pasetemperature(wendu);
                    if (StringUtils.equals("-1.0", pasetemperature)) {
                        pasetemperature = ProbeOutlier.NO_SENSOR_IS_CONNECTED.getCode();
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
                    //已停产,维护以前老设备即可
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
//                    log.info("收到声光报警开启应答成功:" + sn);
//                    redisTemplateUtil.boundValueOps(sn + "lgtalarm").set("1");
                    continue;
                case "96":
//                    log.info("收到声光报警关闭应答成功:" + sn);
//                    redisTemplateUtil.boundValueOps(sn + "lgtalarm").set("2");
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
                case "a3":
                    ParamaterModel paramaterModel15 = cmdidParseUtils.paseA3(cmd, sn, cmdid);
                    list.add(paramaterModel15);
                    continue;
                case "a4":
                    ParamaterModel paramaterModel16 = cmdidParseUtils.paseA4(cmd, sn, cmdid);
                    list.add(paramaterModel16);
                    continue;
                case "a5":
                    ParamaterModel paramaterModel17 = cmdidParseUtils.paseA5(cmd, sn, cmdid);
                    list.add(paramaterModel17);
                    continue;
                case "a6":
                    ParamaterModel paramaterModel18 = cmdidParseUtils.paseA6(cmd, sn, cmdid);
                    list.add(paramaterModel18);
                    continue;
                case "a7":
//                    ParamaterModel  paramaterModel19 = cmdidParseUtils.paseA7(cmd, sn, cmdid);
//                    list.add(paramaterModel19);
                    continue;
                case "a8":
                    ParamaterModel paramaterModel19 = cmdidParseUtils.paseA8(cmd, sn, cmdid);
                    list.add(paramaterModel19);
                    continue;
                case "aa":
                    ParamaterModel paramaterModel20 = cmdidParseUtils.paseAA(cmd, sn, cmdid);
                    list.add(paramaterModel20);
                    continue;
                case "ab":
                    ParamaterModel paramaterModel21 = cmdidParseUtils.paseAB(cmd, sn, cmdid);
                    list.add(paramaterModel21);
                    continue;
                case"b0":
                    ParamaterModel paramaterModel22 = cmdidParseUtils.paseB0(cmd, sn, cmdid);
                    list.add(paramaterModel22);
                    continue;
                case "b1":
                    ParamaterModel paramaterModel23 = cmdidParseUtils.paseB1(cmd, sn, cmdid);
                    list.add(paramaterModel23);
                    continue;
                default:
                    return null;
            }

        }

        return list;
    }


    public static void main(String[] args) {
        ParamaterModel ab = cmdidParseUtils.Paser85("4843850f31373238303530303031c847ffff444223", "2214310001", "85");
        System.out.println(ab);
    }


}