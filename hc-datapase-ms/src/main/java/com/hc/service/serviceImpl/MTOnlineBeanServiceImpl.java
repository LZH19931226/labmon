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
    public ParamaterModel paseData(String cmd) {
            // 获取sn号
            ParamaterModel paramaterModel = new ParamaterModel();
            String sn = HexStringUtils.fromHex(cmd.substring(8, 28));
            String cmdid = cmd.substring(4, 6);
            paramaterModel.setSN(sn);
            paramaterModel.setCmdid(cmdid);
            switch (cmdid) {
                // 心跳包
                case "88":
                    break;
                    // 温度电量
                case "85":
                    paramaterModel = cmdidParseUtils.Paser85(cmd, sn, cmdid);
                    break;
                    // CO2 O2 二氧化碳氧气
                case "87":
                    paramaterModel = cmdidParseUtils.Paser87(cmd, sn, cmdid);
                    break;
                    // 市电
                case "89":
                    String substring3 = cmd.substring(28, 30);
                    String gas3 = electricity(substring3);
                    paramaterModel.setUPS(gas3);
                    break;
                    // 开关门记录
                case "8d":
                    String substring5 = cmd.substring(28, 30);
                    String gas4 = electricity(substring5);
                    paramaterModel.setDOOR(gas4);
                    String substring6 = cmd.substring(30, 32);
                    String gas5 = electricity(substring6);
                    paramaterModel.setDOORZ(gas5);
                    break;
                case "8e":
                    // 湿度
                    String substring7 = cmd.substring(28, 32);
                    if (StringUtils.equalsAnyIgnoreCase(substring7, "ff00")) {
                        paramaterModel.setRH(ProbeOutlier.OUT_OF_TEST_RANGE.getCode());
                    } else {
                        String gas6 = paramaterModelUtils.gas(substring7);
                        gas6 = CustomUtils.agreementAll(gas6, "0", "99");
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
                        gas7 = CustomUtils.agreementAll(gas7, "0", "20");
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
                    break;
                    //模拟500的温度
                case "70":
                    String wendu = cmd.substring(28, 32);
                    String pasetemperature = pasetemperature(wendu);
                    if (StringUtils.equals("-1.0", pasetemperature)) {
                        pasetemperature = ProbeOutlier.NO_SENSOR_IS_CONNECTED.getCode();
                    }
                    paramaterModel.setTEMP(pasetemperature);
                    break;
                    //模拟500的co2
                case "71":
                    String eryang = cmd.substring(28, 32);
                    String s = paseAir(eryang);
                    paramaterModel.setCO2(s);
                    break;
                    //模拟500的RH
                case "72":
                    String rh = cmd.substring(28, 32);
                    String gas8 = paramaterModelUtils.gas(rh);
                    paramaterModel.setRH(gas8);
                    break;
                    //模拟500的VOC
                case "73":
                    String voc = cmd.substring(28, 32);
                    String gas9 = paramaterModelUtils.gas(voc);
                    paramaterModel.setVOC(gas9);
                    break;
                    //模拟500 PM2.5
                case "74":
                    String pm25 = cmd.substring(28, 32);
                    String electricity1 = paramaterModelUtils.gas(pm25);
                    paramaterModel.setPM25(electricity1);
                    break;
                    //模拟500 O2
                case "75":
                    String O2 = cmd.substring(28, 32);
                    String s1 = paseAir(O2);
                    paramaterModel.setO2(s1);
                    break;
                    //模拟500 QC
                case "76":
                    String qc = cmd.substring(28, 32);
                    String electricity6 = paramaterModelUtils.gas(qc);
                    paramaterModel.setQC(electricity6);
                    break;
                    //模拟500 UPS
                case "77":
                    String ups = cmd.substring(28, 32);
                    String electricity7 = paramaterModelUtils.gas(ups);
                    paramaterModel.setUPS(electricity7);
                    break;
                    //模拟500 door
                case "78":
                    String door = cmd.substring(28, 32);
                    String electricity8 = paramaterModelUtils.gas(door);
                    paramaterModel.setDOOR(electricity8);
                    break;
                    //模拟500 OX
                case "79":
                    String jiaquan = cmd.substring(28, 32);
                    String electricity9 = paramaterModelUtils.gas(jiaquan);
                    paramaterModel.setOX(electricity9);
                    break;
                case "7a":
                    String pm10 = cmd.substring(28, 32);
                    String electricity10 = paramaterModelUtils.gas(pm10);
                    paramaterModel.setPM10(electricity10);
                    break;
                case "7b":
                    String qiliu = cmd.substring(28, 32);
                    String electricity11 = paramaterModelUtils.gas(qiliu);
                    paramaterModel.setPRESS(electricity11);
                    break;
                case "91":
                    paramaterModel = cmdidParseUtils.pase91(cmd);
                    paramaterModel.setSN(sn);
                    paramaterModel.setCmdid(cmdid);
                    break;
                case "90":
                    //已停产,维护以前老设备即可
                    paramaterModel = cmdidParseUtils.pase90(cmd);
                    paramaterModel.setSN(sn);
                    paramaterModel.setCmdid(cmdid);
                    break;
                case "92":
                    paramaterModel = cmdidParseUtils.pase92(cmd);
                    paramaterModel.setSN(sn);
                    paramaterModel.setCmdid(cmdid);
                    break;
                case "93":
                    paramaterModel = cmdidParseUtils.pase93(cmd);
                    paramaterModel.setSN(sn);
                    paramaterModel.setCmdid(cmdid);
                    break;
                case "94":
                    paramaterModel = cmdidParseUtils.pase94(cmd);
                    paramaterModel.setSN(sn);
                    paramaterModel.setCmdid(cmdid);
                    break;
                case "95":
//                    log.info("收到声光报警开启应答成功:" + sn);
//                    redisTemplateUtil.boundValueOps(sn + "lgtalarm").set("1");
                    break;
                case "96":
//                    log.info("收到声光报警关闭应答成功:" + sn);
//                    redisTemplateUtil.boundValueOps(sn + "lgtalarm").set("2");
                    break;
                case "97":
                    paramaterModel = cmdidParseUtils.pase97(cmd);
                    paramaterModel.setSN(sn);
                    paramaterModel.setCmdid(cmdid);
                    break;
                case "8a":
                    paramaterModel = cmdidParseUtils.pase8a(cmd);
                    paramaterModel.setSN(sn);
                    paramaterModel.setCmdid(cmdid);
                    break;
                case "8b":
                    paramaterModel = cmdidParseUtils.pase8b(cmd);
                    paramaterModel.setSN(sn);
                    paramaterModel.setCmdid(cmdid);
                    break;
                case "8c":
                    paramaterModel = cmdidParseUtils.pase8c(cmd);
                    paramaterModel.setSN(sn);
                    paramaterModel.setCmdid(cmdid);
                    break;
                case "98":
                    paramaterModel = cmdidParseUtils.pase98(cmd);
                    paramaterModel.setSN(sn);
                    paramaterModel.setCmdid(cmdid);
                    break;
                case "99":
                    paramaterModel = cmdidParseUtils.pase99(cmd);
                    paramaterModel.setSN(sn);
                    paramaterModel.setCmdid(cmdid);
                    break;
                case "9a":
                    paramaterModel = cmdidParseUtils.pase9A(cmd);
                    paramaterModel.setSN(sn);
                    paramaterModel.setCmdid(cmdid);
                    break;
                case "9b":
                    paramaterModel = cmdidParseUtils.pase9b(cmd, sn, cmdid);
                    break;
                case "9c":
                    paramaterModel = cmdidParseUtils.pase9c(cmd, sn, cmdid);
                    break;
                case "9f":
                    paramaterModel = cmdidParseUtils.pase9f(cmd, sn, cmdid);
                    break;
                case "a1":
                    paramaterModel = cmdidParseUtils.paseA1(cmd, sn, cmdid);
                    break;
                case "a2":
                    paramaterModel = cmdidParseUtils.paseA2(cmd, sn, cmdid);
                    break;
                case "a3":
                    paramaterModel = cmdidParseUtils.paseA3(cmd, sn, cmdid);
                    break;
                case "a4":
                    paramaterModel = cmdidParseUtils.paseA4(cmd, sn, cmdid);
                    break;
                case "a5":
                    paramaterModel = cmdidParseUtils.paseA5(cmd, sn, cmdid);
                    break;
                case "a6":
                    paramaterModel = cmdidParseUtils.paseA6(cmd, sn, cmdid);
                    break;
                case "a7":
//                    ParamaterModel  paramaterModel19 = cmdidParseUtils.paseA7(cmd, sn, cmdid);
//                    list.add(paramaterModel19);
                    break;
                case "a8":
                    paramaterModel = cmdidParseUtils.paseA8(cmd, sn, cmdid);
                    break;
                case "aa":
                    paramaterModel = cmdidParseUtils.paseAA(cmd, sn, cmdid);
                    break;
                case "ab":
                    paramaterModel = cmdidParseUtils.paseAB(cmd, sn, cmdid);
                    break;
                case"b0":
                    paramaterModel = cmdidParseUtils.paseB0(cmd, sn, cmdid);
                    break;
                case "b1":
                    paramaterModel = cmdidParseUtils.paseB1(cmd, sn, cmdid);
                    break;
                case "b2":
                    paramaterModel = cmdidParseUtils.paseB2(cmd, sn, cmdid);
                    break;
                case "b3":
                    paramaterModel = cmdidParseUtils.paseB3(cmd, sn, cmdid);
                    break;
                default:
                    return null;
            }
        return paramaterModel;
    }


    public static void main(String[] args) {
        ParamaterModel ab = cmdidParseUtils.paseB1("4843b11a32333032343830303132006116e1806e80c80001000064004455e823", "2302480005", "11");
        System.out.println(ab);
    }


}
