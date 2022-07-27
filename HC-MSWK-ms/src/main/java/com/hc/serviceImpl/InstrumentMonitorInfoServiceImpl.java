package com.hc.serviceImpl;

import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.device.ProbeRedisApi;
import com.hc.model.WarningMqModel;
import com.hc.my.common.core.constant.enums.CurrentProbeInfoEnum;
import com.hc.my.common.core.constant.enums.ProbeOutlierMt310;
import com.hc.my.common.core.redis.dto.ParamaterModel;
import com.hc.my.common.core.constant.enums.ProbeOutlier;
import com.hc.my.common.core.redis.dto.InstrumentInfoDto;
import com.hc.my.common.core.redis.dto.ProbeInfoDto;
import com.hc.my.common.core.util.RegularUtil;
import com.hc.po.Monitorinstrument;
import com.hc.service.InstrumentMonitorInfoService;
import com.hc.service.LastDataService;
import com.hc.utils.JsonUtil;
import com.hc.utils.ShowModelUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class InstrumentMonitorInfoServiceImpl implements InstrumentMonitorInfoService {

    DecimalFormat df = new DecimalFormat("######0.00");

    @Autowired
    private LastDataService lastDataService;
    @Autowired
    private ShowModelUtils showModelUtils;
    @Autowired
    private ProbeRedisApi probeRedisApi;

    @Override
    public List<WarningMqModel> save(ParamaterModel model, Monitorinstrument monitorinstrument) {
        //命令id
        Monitorequipmentlastdata monitorequipmentlastdata = new Monitorequipmentlastdata();
        String equipmentno = monitorinstrument.getEquipmentno();
        String hospitalcode = monitorinstrument.getHospitalcode();
        String instrumentno = monitorinstrument.getInstrumentno();

        if (StringUtils.isEmpty(equipmentno) || StringUtils.isEmpty(monitorinstrument.getInstrumentno())) {
            return null;
        }
        /**
         *   数据插入到redis中，做超时设置： （需要做超时报警医院从redis里面获取）
         */
        String sn = model.getSN();

        List<WarningMqModel> list = new ArrayList<>();
        String cmdid = model.getCmdid();
        String snType = sn.substring(4, 6);
        switch (cmdid) {
            case "85":
                InstrumentInfoDto probe = probeRedisApi.getProbeRedisInfo(hospitalcode, instrumentno + ":4").getResult();
                if(!ObjectUtils.isEmpty(probe)){
                    if (StringUtils.equals("04", snType)) {
                        if (StringUtils.isNotEmpty(model.getTEMP2())) {
                            String calibration = showModelUtils.calibration(probe, model.getTEMP2());
                            // 判断是否存在温度探头
                            monitorequipmentlastdata.setCurrenttemperature(calibration);
                            BuildProbeInfoDto(hospitalcode,equipmentno,
                                    CurrentProbeInfoEnum.CURRENT_TEMPERATURE.getInstrumentConfigId(),calibration,
                                    CurrentProbeInfoEnum.CURRENT_TEMPERATURE.getProbeEName());
                            //执行报警服务
                            WarningMqModel warningMqModel = showModelUtils.procWarnModel(calibration, monitorinstrument, model.getNowTime(), 4, "温度");
                            list.add(warningMqModel);
                        }
                    } else {
                        if (StringUtils.isNotEmpty(model.getTEMP())) {
                            String calibration = showModelUtils.calibration(probe, model.getTEMP());
                            if (!StringUtils.equalsAny(calibration, "0", "0.00", "0.0")) {
                                monitorequipmentlastdata.setCurrenttemperature(calibration);
                                BuildProbeInfoDto(hospitalcode,equipmentno,
                                        CurrentProbeInfoEnum.CURRENT_TEMPERATURE.getInstrumentConfigId(),calibration,
                                        CurrentProbeInfoEnum.CURRENT_TEMPERATURE.getProbeEName());
                                WarningMqModel warningMqModel = showModelUtils.procWarnModel(calibration, monitorinstrument, model.getNowTime(), 4, "温度");
                                list.add(warningMqModel);
                            }
                        }
                    }
                    if (StringUtils.isNotEmpty(model.getQC()) && !StringUtils.equals(model.getQC(), "0")) {
                        String qc = model.getQC();
                        if (StringUtils.endsWithAny(snType, "02", "18")) {
                            //液氮罐锁电量
                            monitorequipmentlastdata.setCurrentqcl(qc);
                            BuildProbeInfoDto(hospitalcode,equipmentno,
                                    CurrentProbeInfoEnum.CURRENTQCL.getInstrumentConfigId(),qc,
                                    CurrentProbeInfoEnum.CURRENTQCL.getProbeEName());
                        } else {
                            //常规液氮罐电量
                            monitorequipmentlastdata.setCurrentqc(qc);
                            BuildProbeInfoDto(hospitalcode,equipmentno,
                                    CurrentProbeInfoEnum.CURRENTQC.getInstrumentConfigId(),qc,
                                    CurrentProbeInfoEnum.CURRENTQC.getProbeEName());
                        }
                        WarningMqModel warningMqModel = showModelUtils.procWarnModel(model.getQC(), monitorinstrument, model.getNowTime(), 7, "电量");
                        list.add(warningMqModel);
                    }
                }
                break;
            case "87":
                // CO2 O2 二氧化碳氧气
                InstrumentInfoDto probeCO2 = probeRedisApi.getProbeRedisInfo(hospitalcode, instrumentno + ":1").getResult();
                InstrumentInfoDto probeO2 = probeRedisApi.getProbeRedisInfo(hospitalcode, instrumentno + ":2").getResult();
                if (StringUtils.isNotEmpty(model.getCO2()) && null != probeCO2) {
                    String calibration = showModelUtils.calibration(probeCO2, model.getCO2());
                    monitorequipmentlastdata.setCurrentcarbondioxide(calibration);
                    BuildProbeInfoDto(hospitalcode,equipmentno,
                            CurrentProbeInfoEnum.CURRENTCARBONDIOXIDE.getInstrumentConfigId(),calibration,
                            CurrentProbeInfoEnum.CURRENTCARBONDIOXIDE.getProbeEName());
                    WarningMqModel warningMqModel = showModelUtils.procWarnModel(calibration, monitorinstrument, model.getNowTime(), 1, "CO2");
                    list.add(warningMqModel);
                }
                if (StringUtils.isNotEmpty(model.getO2()) && null!=probeO2) {
                    String calibration = showModelUtils.calibration(probeO2, model.getO2());
                    monitorequipmentlastdata.setCurrento2(calibration);
                    BuildProbeInfoDto(hospitalcode,equipmentno,
                            CurrentProbeInfoEnum.CURRENTO2.getInstrumentConfigId(),calibration,
                            CurrentProbeInfoEnum.CURRENTO2.getProbeEName());
                    WarningMqModel warningMqModel = showModelUtils.procWarnModel(calibration, monitorinstrument, model.getNowTime(), 2, "O2");
                    list.add(warningMqModel);
                }
                break;
            case "89":
                if (!StringUtils.isEmpty(model.getUPS())) {
                    String ups = "1"; // 表示市电异常
                    if ("3".equals(model.getUPS()) || "4".equals(model.getUPS())) {
                        ups = "0";//市电正常
                    }
                    monitorequipmentlastdata.setCurrentups(ups);
                    BuildProbeInfoDto(hospitalcode,equipmentno,
                            CurrentProbeInfoEnum.CURRENTUPS.getInstrumentConfigId(),ups,
                            CurrentProbeInfoEnum.CURRENTUPS.getProbeEName());
                    WarningMqModel warningMqModel = showModelUtils.procWarnModel(ups, monitorinstrument, model.getNowTime(), 10, "市电");
                    list.add(warningMqModel);
                }
                break;
            case "8d":
                //开关门记录
                //查询是否存在开门量（报警信息探头）
                InstrumentInfoDto dd = probeRedisApi.getProbeRedisInfo(hospitalcode, instrumentno + ":11").getResult();
                if (StringUtils.isNotEmpty(model.getDOORZ()) && !ObjectUtils.isEmpty(dd)) {
                    String DOOR;
                    if ("3".equals(model.getDOORZ()) || "1".equals(model.getDOORZ())) {
                        //表示关门
                        DOOR = "0";
                    } else {
                        DOOR = "1";
                        //表示开门
                    }
                    monitorequipmentlastdata.setCurrentdoorstate(DOOR);
                    BuildProbeInfoDto(hospitalcode,equipmentno,
                            CurrentProbeInfoEnum.CURRENTDOORSTATE.getInstrumentConfigId(),DOOR,
                            CurrentProbeInfoEnum.CURRENTDOORSTATE.getProbeEName());
                    WarningMqModel warningMqModel = showModelUtils.procWarnModel(DOOR, monitorinstrument, model.getNowTime(), 11, "DOOR");
                    list.add(warningMqModel);
                } else {
                    return null;
                }
                break;
            case "8e":
                // 3 :VOC 8: PM2.5 9: PM10 12: 甲醛 6: 压力   5:湿度
                InstrumentInfoDto voc = probeRedisApi.getProbeRedisInfo(hospitalcode, instrumentno + ":3").getResult();
                InstrumentInfoDto pm25 = probeRedisApi.getProbeRedisInfo(hospitalcode, instrumentno + ":8").getResult();
                InstrumentInfoDto pm10 = probeRedisApi.getProbeRedisInfo(hospitalcode, instrumentno + ":9").getResult();
                InstrumentInfoDto jq = probeRedisApi.getProbeRedisInfo(hospitalcode, instrumentno + ":12").getResult();
                InstrumentInfoDto yl = probeRedisApi.getProbeRedisInfo(hospitalcode, instrumentno + ":6").getResult();
                InstrumentInfoDto sd = probeRedisApi.getProbeRedisInfo(hospitalcode, instrumentno + ":5").getResult();
                if (StringUtils.isNotEmpty(model.getVOC()) && null!=voc) {
                    String calibration = showModelUtils.calibration(voc, model.getVOC());
                    monitorequipmentlastdata.setCurrentvoc(calibration);
                    BuildProbeInfoDto(hospitalcode,equipmentno,
                            CurrentProbeInfoEnum.CURRENTVOC.getInstrumentConfigId(),calibration,
                            CurrentProbeInfoEnum.CURRENTVOC.getProbeEName());
                    WarningMqModel warningMqModel = showModelUtils.procWarnModel(calibration, monitorinstrument, model.getNowTime(), 3, "空气质量");
                    list.add(warningMqModel);
                }
                if (StringUtils.isNotEmpty(model.getOX()) && null!=jq) {
                    String calibration = showModelUtils.calibration(jq, model.getOX());
                    monitorequipmentlastdata.setCurrentformaldehyde(calibration);
                    BuildProbeInfoDto(hospitalcode,equipmentno,
                            CurrentProbeInfoEnum.CURRENTFORMALDEHYDE.getInstrumentConfigId(),calibration,
                            CurrentProbeInfoEnum.CURRENTFORMALDEHYDE.getProbeEName());
                    WarningMqModel warningMqModel = showModelUtils.procWarnModel(calibration, monitorinstrument, model.getNowTime(), 12, "甲醛");
                    list.add(warningMqModel);
                }
                if (StringUtils.isNotEmpty(model.getPM10()) && null!=pm10) {
                    String calibration = showModelUtils.calibration(pm10, model.getPM10());
                    monitorequipmentlastdata.setCurrentpm10(calibration);
                    BuildProbeInfoDto(hospitalcode,equipmentno,
                            CurrentProbeInfoEnum.CURRENTPM10.getInstrumentConfigId(),calibration,
                            CurrentProbeInfoEnum.CURRENTPM10.getProbeEName());
                    WarningMqModel warningMqModel = showModelUtils.procWarnModel(calibration, monitorinstrument, model.getNowTime(), 9, "PM10");
                    list.add(warningMqModel);
                }
                if (StringUtils.isNotEmpty(model.getPM25()) && null!=pm25) {
                    String calibration = showModelUtils.calibration(pm25, model.getPM25());
                    monitorequipmentlastdata.setCurrentpm25(calibration);
                    BuildProbeInfoDto(hospitalcode,equipmentno,
                            CurrentProbeInfoEnum.CURRENTPM25.getInstrumentConfigId(),calibration,
                            CurrentProbeInfoEnum.CURRENTPM25.getProbeEName());
                    WarningMqModel warningMqModel = showModelUtils.procWarnModel(calibration, monitorinstrument, model.getNowTime(), 8, "PM2.5");
                    list.add(warningMqModel);
                }
                if (StringUtils.isNotEmpty(model.getPRESS()) && null!=yl) {
                    String calibration = showModelUtils.calibration(yl, model.getPRESS());
                    if (!StringUtils.equalsAny(calibration, "0.0", "0.00", "0")) {
                        monitorequipmentlastdata.setCurrentairflow(calibration);
                        BuildProbeInfoDto(hospitalcode,equipmentno,
                                CurrentProbeInfoEnum.CURRENTAIRFLOW.getInstrumentConfigId(),calibration,
                                CurrentProbeInfoEnum.CURRENTAIRFLOW.getProbeEName());
                        WarningMqModel warningMqModel = showModelUtils.procWarnModel(calibration, monitorinstrument, model.getNowTime(), 6, "压力");
                        list.add(warningMqModel);
                    }
                }
                if (StringUtils.isNotEmpty(model.getRH()) && null!=sd) {
                    String calibration = showModelUtils.calibration(sd, model.getRH());
                    if (!StringUtils.equalsAny(calibration, "0", "0.0", "0.00")) {
                        monitorequipmentlastdata.setCurrenthumidity(calibration);
                        BuildProbeInfoDto(hospitalcode,equipmentno,
                                CurrentProbeInfoEnum.CURRENTHUMIDITY.getInstrumentConfigId(),calibration,
                                CurrentProbeInfoEnum.CURRENTHUMIDITY.getProbeEName());
                        WarningMqModel warningMqModel = showModelUtils.procWarnModel(calibration, monitorinstrument, model.getNowTime(), 5, "湿度");
                        list.add(warningMqModel);
                    }
                }
                break;
            case "91":
                // CO2 O2 温度
                InstrumentInfoDto o3 = probeRedisApi.getProbeRedisInfo(hospitalcode, instrumentno + ":1").getResult();
                InstrumentInfoDto o4 = probeRedisApi.getProbeRedisInfo(hospitalcode, instrumentno + ":2").getResult();
                InstrumentInfoDto o5 = probeRedisApi.getProbeRedisInfo(hospitalcode, instrumentno + ":4").getResult();
                if (StringUtils.isNotEmpty(model.getCO2()) && !ObjectUtils.isEmpty(o3)) {
                    String calibration = showModelUtils.calibration(o3, model.getCO2());
                    monitorequipmentlastdata.setCurrentcarbondioxide(calibration);
                    BuildProbeInfoDto(hospitalcode,equipmentno,
                            CurrentProbeInfoEnum.CURRENTCARBONDIOXIDE.getInstrumentConfigId(),calibration,
                            CurrentProbeInfoEnum.CURRENTCARBONDIOXIDE.getProbeEName());
                    WarningMqModel warningMqModel = showModelUtils.procWarnModel(calibration, monitorinstrument, model.getNowTime(), 1, "CO2");
                    list.add(warningMqModel);
                }
                if (StringUtils.isNotEmpty(model.getO2()) && !ObjectUtils.isEmpty(o4)) {
                    String calibration = showModelUtils.calibration(o4, model.getO2());
                    monitorequipmentlastdata.setCurrento2(calibration);
                    BuildProbeInfoDto(hospitalcode,equipmentno,
                            CurrentProbeInfoEnum.CURRENTO2.getInstrumentConfigId(),calibration,
                            CurrentProbeInfoEnum.CURRENTO2.getProbeEName());
                    WarningMqModel warningMqModel = showModelUtils.procWarnModel(calibration, monitorinstrument, model.getNowTime(), 2, "O2");
                    list.add(warningMqModel);
                }
                if (StringUtils.isNotEmpty(model.getTEMP()) &&  !ObjectUtils.isEmpty(o5)) {
                    String calibration = showModelUtils.calibration(o5, model.getTEMP());
                    monitorequipmentlastdata.setCurrenttemperature(calibration);
                    BuildProbeInfoDto(hospitalcode,equipmentno,
                            CurrentProbeInfoEnum.CURRENT_TEMPERATURE.getInstrumentConfigId(),calibration,
                            CurrentProbeInfoEnum.CURRENT_TEMPERATURE.getProbeEName());
                    WarningMqModel warningMqModel = showModelUtils.procWarnModel(calibration, monitorinstrument, model.getNowTime(), 4, "温度");
                    list.add(warningMqModel);
                }
                break;
            case "70":
                //模拟500的温度
                InstrumentInfoDto o7 = probeRedisApi.getProbeRedisInfo(hospitalcode, instrumentno + ":4").getResult();
                if ( !ObjectUtils.isEmpty(o7) && StringUtils.isNotEmpty(model.getTEMP())) {
                    String calibration = showModelUtils.calibration(o7, model.getTEMP());
                    monitorequipmentlastdata.setCurrenttemperature(calibration);
                    BuildProbeInfoDto(hospitalcode,equipmentno,
                            CurrentProbeInfoEnum.CURRENT_TEMPERATURE.getInstrumentConfigId(),calibration,
                            CurrentProbeInfoEnum.CURRENT_TEMPERATURE.getProbeEName());
                    WarningMqModel warningMqModel = showModelUtils.procWarnModel(calibration, monitorinstrument, model.getNowTime(), 4, "温度");
                    list.add(warningMqModel);
                }
                break;
            case "71":
                //模拟500的co2
                monitorequipmentlastdata.setCurrentcarbondioxide(null);
                InstrumentInfoDto o6 = probeRedisApi.getProbeRedisInfo(hospitalcode, instrumentno + ":1").getResult();
                if ( !ObjectUtils.isEmpty(o6) && StringUtils.isNotEmpty(model.getCO2())) {
                    String calibration = showModelUtils.calibration(o6, model.getCO2());
                    monitorequipmentlastdata.setCurrentcarbondioxide(calibration);
                    BuildProbeInfoDto(hospitalcode,equipmentno,
                            CurrentProbeInfoEnum.CURRENTCARBONDIOXIDE.getInstrumentConfigId(),calibration,
                            CurrentProbeInfoEnum.CURRENTCARBONDIOXIDE.getProbeEName());
                    WarningMqModel warningMqModel1 = showModelUtils.procWarnModel(calibration, monitorinstrument, model.getNowTime(), 1, "CO2");
                    list.add(warningMqModel1);
                }
                break;
            case "72":
                //模拟500的RH
                monitorequipmentlastdata.setCurrenthumidity(model.getRH());
                BuildProbeInfoDto(hospitalcode,equipmentno,
                        CurrentProbeInfoEnum.CURRENTHUMIDITY.getInstrumentConfigId(),model.getRH(),
                        CurrentProbeInfoEnum.CURRENTHUMIDITY.getProbeEName());
                WarningMqModel warningMqModel2 = showModelUtils.procWarnModel(model.getRH(), monitorinstrument, model.getNowTime(), 5, "湿度");
                list.add(warningMqModel2);
                break;
            case "73":
                //模拟500的VOC
                String voc1 = model.getVOC();
                Double integer = new Double(voc1);
                if (StringUtils.endsWithAny(sn, "1805990159", "1806990041", "1810990160")) {
                    voc1 = chu(integer, "1000");
                }
                monitorequipmentlastdata.setCurrentvoc(voc1);
                BuildProbeInfoDto(hospitalcode,equipmentno,
                        CurrentProbeInfoEnum.CURRENTVOC.getInstrumentConfigId(),voc1,
                        CurrentProbeInfoEnum.CURRENTVOC.getProbeEName());
                WarningMqModel warningMqModel3 = showModelUtils.procWarnModel(voc1, monitorinstrument, model.getNowTime(), 3, "空气质量");
                list.add(warningMqModel3);
                break;
            case "74":
                //模拟500 PM2.5
                String pm251 = model.getPM25();
                double integer2 = Double.parseDouble(pm251);
                if (integer2 < 0.1) {
                    integer2 = integer2 * 100;
                }
                monitorequipmentlastdata.setCurrentpm25(Double.toString(integer2));
                BuildProbeInfoDto(hospitalcode,equipmentno,
                        CurrentProbeInfoEnum.CURRENTPM25.getInstrumentConfigId(),Double.toString(integer2),
                        CurrentProbeInfoEnum.CURRENTPM25.getProbeEName());
                WarningMqModel warningMqModel4 = showModelUtils.procWarnModel(Double.toString(integer2), monitorinstrument, model.getNowTime(), 8, "PM2.5");
                list.add(warningMqModel4);
                break;
            case "75":
                //模拟500 O2
                monitorequipmentlastdata.setCurrento2(null);
                InstrumentInfoDto o8 = probeRedisApi.getProbeRedisInfo(hospitalcode, instrumentno + ":2").getResult();
                if ( !ObjectUtils.isEmpty(o8)) {
                    monitorequipmentlastdata.setCurrento2(model.getO2());
                    BuildProbeInfoDto(hospitalcode,equipmentno,
                            CurrentProbeInfoEnum.CURRENTO2.getInstrumentConfigId(),model.getO2(),
                            CurrentProbeInfoEnum.CURRENTO2.getProbeEName());
                    WarningMqModel warningMqModel5 = showModelUtils.procWarnModel(model.getO2(), monitorinstrument, model.getNowTime(), 2, "O2");
                    list.add(warningMqModel5);
                }
                break;
            case "76":
                //模拟500 QC
                monitorequipmentlastdata.setCurrentqc(model.getQC());
                BuildProbeInfoDto(hospitalcode,equipmentno,
                        CurrentProbeInfoEnum.CURRENTQC.getInstrumentConfigId(),model.getQC(),
                        CurrentProbeInfoEnum.CURRENTQC.getProbeEName());
                WarningMqModel warningMqModel6 = showModelUtils.procWarnModel(model.getQC(), monitorinstrument, model.getNowTime(), 7, "电量");
                list.add(warningMqModel6);
                break;
            case "77":
                //模拟500 UPS
                String ups = "0";
                if ("3".equals(model.getUPS()) || "4".equals(model.getUPS())) {
                    ups = "1";
                }
                monitorequipmentlastdata.setCurrentups(ups);
                BuildProbeInfoDto(hospitalcode,equipmentno,
                        CurrentProbeInfoEnum.CURRENTUPS.getInstrumentConfigId(),ups,
                        CurrentProbeInfoEnum.CURRENTUPS.getProbeEName());
                WarningMqModel warningMqModel7 = showModelUtils.procWarnModel(model.getUPS(), monitorinstrument, model.getNowTime(), 10, "市电");
                list.add(warningMqModel7);
                break;
            case "78":
                if (!StringUtils.isEmpty(model.getDOOR())) {
                    String DOOR = "0";
                    if (StringUtils.equalsAny("0.00", "0.0", "0", model.getDOOR())) {
                        //表示无报警信息
                        DOOR = "0";
                    } else {
                        DOOR = "1";
                        //表示有报警信息
                    }
                    monitorequipmentlastdata.setCurrentdoorstate(DOOR);
                    BuildProbeInfoDto(hospitalcode,equipmentno,
                            CurrentProbeInfoEnum.CURRENTDOORSTATE.getInstrumentConfigId(),DOOR,
                            CurrentProbeInfoEnum.CURRENTDOORSTATE.getProbeEName());
                    WarningMqModel warningMqModel8 = showModelUtils.procWarnModel(DOOR, monitorinstrument, model.getNowTime(), 11, "DOOR");
                    list.add(warningMqModel8);
                }
                break;
            case "79":
                //模拟500 OX
                monitorequipmentlastdata.setCurrentformaldehyde(model.getOX());
                BuildProbeInfoDto(hospitalcode,equipmentno,
                        CurrentProbeInfoEnum.CURRENTFORMALDEHYDE.getInstrumentConfigId(),model.getOX(),
                        CurrentProbeInfoEnum.CURRENTFORMALDEHYDE.getProbeEName());
                WarningMqModel warningMqModel9 = showModelUtils.procWarnModel(model.getOX(), monitorinstrument, model.getNowTime(), 12, "甲醛");
                list.add(warningMqModel9);
                break;
            case "7a":
                String pm101 = model.getPM10();
                double v = Double.parseDouble(pm101);
                if (v < 0.1) {
                    v = v * 100;
                }
                monitorequipmentlastdata.setCurrentpm10(Double.toString(v));
                BuildProbeInfoDto(hospitalcode,equipmentno,
                        CurrentProbeInfoEnum.CURRENTPM10.getInstrumentConfigId(),Double.toString(v),
                        CurrentProbeInfoEnum.CURRENTPM10.getProbeEName());
                WarningMqModel warningMqModel10 = showModelUtils.procWarnModel(Double.toString(v), monitorinstrument, model.getNowTime(), 9, "PM10");
                list.add(warningMqModel10);
                break;
            case "7b":
                // 模拟500 压力  或者是培养箱气流
                String press = model.getPRESS();
                double integer1 = Double.parseDouble(press);
                if (integer1 < 100) {
                    integer1 = integer1 * 100;
                }
                monitorequipmentlastdata.setCurrentairflow(Double.toString(integer1));
                BuildProbeInfoDto(hospitalcode,equipmentno,
                        CurrentProbeInfoEnum.CURRENTAIRFLOW.getInstrumentConfigId(),Double.toString(integer1),
                        CurrentProbeInfoEnum.CURRENTAIRFLOW.getProbeEName());
                WarningMqModel warningMqModel11 = showModelUtils.procWarnModel(Double.toString(integer1), monitorinstrument, model.getNowTime(), 6, "压力");
                list.add(warningMqModel11);
                break;
            case "92":
                InstrumentInfoDto left = probeRedisApi.getProbeRedisInfo(hospitalcode, instrumentno + ":23").getResult();
                InstrumentInfoDto right = probeRedisApi.getProbeRedisInfo(hospitalcode, instrumentno + ":24").getResult();
                InstrumentInfoDto airflow = probeRedisApi.getProbeRedisInfo(hospitalcode, instrumentno + ":25").getResult();
                if (StringUtils.isNotEmpty(model.getTEMP()) &&  !ObjectUtils.isEmpty(left)) {
                    monitorequipmentlastdata.setCurrentlefttemperature(model.getTEMP());
                    BuildProbeInfoDto(hospitalcode,equipmentno,
                            CurrentProbeInfoEnum.CURRENTLEFTTEMPERATURE.getInstrumentConfigId(),model.getTEMP(),
                            CurrentProbeInfoEnum.CURRENTLEFTTEMPERATURE.getProbeEName());
                    WarningMqModel warningMqModel = showModelUtils.procWarnModel(model.getTEMP(), monitorinstrument, model.getNowTime(), 23, "左舱室温度");
                    list.add(warningMqModel);
                }
                if (StringUtils.isNotEmpty(model.getTEMP2()) &&  !ObjectUtils.isEmpty(right)) {
                    monitorequipmentlastdata.setCurrentrigthtemperature(model.getTEMP2());
                    BuildProbeInfoDto(hospitalcode,equipmentno,
                            CurrentProbeInfoEnum.CURRENTRIGTHTEMPERATURE.getInstrumentConfigId(),model.getTEMP2(),
                            CurrentProbeInfoEnum.CURRENTRIGTHTEMPERATURE.getProbeEName());
                    WarningMqModel warningMqModel = showModelUtils.procWarnModel(model.getTEMP2(), monitorinstrument, model.getNowTime(), 24, "右舱室温度");
                    list.add(warningMqModel);
                }
                if (StringUtils.isNotEmpty(model.getAirflow()) &&  !ObjectUtils.isEmpty(airflow)) {
                    monitorequipmentlastdata.setCurrentairflow1(model.getAirflow());
                    BuildProbeInfoDto(hospitalcode,equipmentno,
                            CurrentProbeInfoEnum.CURRENTAIRFLOW1.getInstrumentConfigId(),model.getAirflow(),
                            CurrentProbeInfoEnum.CURRENTAIRFLOW1.getProbeEName());
                    WarningMqModel warningMqModel = showModelUtils.procWarnModel(model.getAirflow(), monitorinstrument, model.getNowTime(), 25, "气流");
                    list.add(warningMqModel);
                }
                break;
            // C60培养箱
            case "93":
                InstrumentInfoDto yq = probeRedisApi.getProbeRedisInfo(hospitalcode, instrumentno + ":2").getResult();
                monitorequipmentlastdata.setCurrenttemperature(model.getTEMP());
                BuildProbeInfoDto(hospitalcode,equipmentno,
                        CurrentProbeInfoEnum.CURRENT_TEMPERATURE.getInstrumentConfigId(),model.getTEMP(),
                        CurrentProbeInfoEnum.CURRENT_TEMPERATURE.getProbeEName());
                WarningMqModel warningMqModel = showModelUtils.procWarnModel(model.getTEMP(), monitorinstrument, model.getNowTime(), 4, "温度");
                list.add(warningMqModel);
                if (StringUtils.isNotEmpty(model.getO2()) && null!=yq) {
                    monitorequipmentlastdata.setCurrento2(model.getO2());
                    BuildProbeInfoDto(hospitalcode,equipmentno,
                            CurrentProbeInfoEnum.CURRENTO2.getInstrumentConfigId(),model.getO2(),
                            CurrentProbeInfoEnum.CURRENTO2.getProbeEName());
                    WarningMqModel warningMqModel1 = showModelUtils.procWarnModel(model.getO2(), monitorinstrument, model.getNowTime(), 2, "O2");
                    list.add(warningMqModel1);
                }
                monitorequipmentlastdata.setCurrentcarbondioxide(model.getCO2());
                BuildProbeInfoDto(hospitalcode,equipmentno,
                        CurrentProbeInfoEnum.CURRENTCARBONDIOXIDE.getInstrumentConfigId(),model.getCO2(),
                        CurrentProbeInfoEnum.CURRENTCARBONDIOXIDE.getProbeEName());
                WarningMqModel warningMqModel22 = showModelUtils.procWarnModel(model.getCO2(), monitorinstrument, model.getNowTime(), 1, "CO2");
                list.add(warningMqModel22);
                monitorequipmentlastdata.setCurrenthumidity(model.getRH());
                BuildProbeInfoDto(hospitalcode,equipmentno,
                        CurrentProbeInfoEnum.CURRENTHUMIDITY.getInstrumentConfigId(),model.getRH(),
                        CurrentProbeInfoEnum.CURRENTHUMIDITY.getProbeEName());
                WarningMqModel warningMqModel23 = showModelUtils.procWarnModel(model.getRH(), monitorinstrument, model.getNowTime(), 5, "RH");
                list.add(warningMqModel23);
                break;
            // G185培养箱
            case "94":
                monitorequipmentlastdata.setCurrento2(model.getO2());
                monitorequipmentlastdata.setCurrentcarbondioxide(model.getCO2());
                monitorequipmentlastdata.setCurrenttemperature1(model.getTEMP());
                monitorequipmentlastdata.setCurrenttemperature2(model.getTEMP2());
                monitorequipmentlastdata.setCurrenttemperature3(model.getTEMP3());
                monitorequipmentlastdata.setCurrenttemperature4(model.getTEMP4());
                monitorequipmentlastdata.setCurrenttemperature5(model.getTEMP5());
                BuildProbeInfoDto(hospitalcode,equipmentno,
                        CurrentProbeInfoEnum.CURRENTO2.getInstrumentConfigId(),model.getO2(),
                        CurrentProbeInfoEnum.CURRENTO2.getProbeEName());
                BuildProbeInfoDto(hospitalcode,equipmentno,
                        CurrentProbeInfoEnum.CURRENTCARBONDIOXIDE.getInstrumentConfigId(),model.getCO2(),
                        CurrentProbeInfoEnum.CURRENTCARBONDIOXIDE.getProbeEName());
                BuildProbeInfoDto(hospitalcode,equipmentno,
                        CurrentProbeInfoEnum.CURRENTTEMPERATURE1.getInstrumentConfigId(),model.getTEMP(),
                        CurrentProbeInfoEnum.CURRENTTEMPERATURE1.getProbeEName());
                BuildProbeInfoDto(hospitalcode,equipmentno,
                        CurrentProbeInfoEnum.CURRENTTEMPERATURE2.getInstrumentConfigId(),model.getTEMP2(),
                        CurrentProbeInfoEnum.CURRENTTEMPERATURE2.getProbeEName());
                BuildProbeInfoDto(hospitalcode,equipmentno,
                        CurrentProbeInfoEnum.CURRENTTEMPERATURE3.getInstrumentConfigId(),model.getTEMP3(),
                        CurrentProbeInfoEnum.CURRENTTEMPERATURE3.getProbeEName());
                BuildProbeInfoDto(hospitalcode,equipmentno,
                        CurrentProbeInfoEnum.CURRENTTEMPERATURE4.getInstrumentConfigId(),model.getTEMP4(),
                        CurrentProbeInfoEnum.CURRENTTEMPERATURE4.getProbeEName());
                BuildProbeInfoDto(hospitalcode,equipmentno,
                        CurrentProbeInfoEnum.CURRENTTEMPERATURE5.getInstrumentConfigId(),model.getTEMP5(),
                        CurrentProbeInfoEnum.CURRENTTEMPERATURE5.getProbeEName());
                WarningMqModel warningMqModel8 = showModelUtils.procWarnModel(model.getTEMP(), monitorinstrument, model.getNowTime(), 13, "一路温度");
                WarningMqModel warningMqModel16 = showModelUtils.procWarnModel(model.getTEMP2(), monitorinstrument, model.getNowTime(), 14, "二路温度");
                WarningMqModel warningMqModel19 = showModelUtils.procWarnModel(model.getTEMP3(), monitorinstrument, model.getNowTime(), 15, "三路温度");
                WarningMqModel warningMqModel20 = showModelUtils.procWarnModel(model.getTEMP4(), monitorinstrument, model.getNowTime(), 16, "四路温度");
                WarningMqModel warningMqModel17 = showModelUtils.procWarnModel(model.getTEMP5(), monitorinstrument, model.getNowTime(), 17, "五路温度");
                WarningMqModel warningMqModel27 = showModelUtils.procWarnModel(model.getCO2(), monitorinstrument, model.getNowTime(), 1, "CO2");
                WarningMqModel warningMqModel37 = showModelUtils.procWarnModel(model.getO2(), monitorinstrument, model.getNowTime(), 2, "O2");
                list.add(warningMqModel8);
                list.add(warningMqModel16);
                list.add(warningMqModel19);
                list.add(warningMqModel20);
                list.add(warningMqModel17);
                list.add(warningMqModel27);
                list.add(warningMqModel37);
                break;
            case "90":
                monitorequipmentlastdata.setCurrenttemperature(model.getTEMP());  //液氮罐温度
                BuildProbeInfoDto(hospitalcode,equipmentno,
                        CurrentProbeInfoEnum.CURRENT_TEMPERATURE.getInstrumentConfigId(),model.getTEMP(),
                        CurrentProbeInfoEnum.CURRENT_TEMPERATURE.getProbeEName());
                WarningMqModel warningMqMode90 = showModelUtils.procWarnModel(model.getTEMP(), monitorinstrument, model.getNowTime(), 4, "温度");
                list.add(warningMqMode90);
                monitorequipmentlastdata.setCurrenttemperature2(model.getTEMP2());// 室温
                BuildProbeInfoDto(hospitalcode,equipmentno,
                        CurrentProbeInfoEnum.CURRENTTEMPERATURE2.getInstrumentConfigId(),model.getTEMP2(),
                        CurrentProbeInfoEnum.CURRENTTEMPERATURE2.getProbeEName());
                monitorequipmentlastdata.setCurrenttemperature3(model.getTEMP3()); // 壁温
                BuildProbeInfoDto(hospitalcode,equipmentno,
                        CurrentProbeInfoEnum.CURRENTTEMPERATURE3.getInstrumentConfigId(),model.getTEMP3(),
                        CurrentProbeInfoEnum.CURRENTTEMPERATURE3.getProbeEName());
                monitorequipmentlastdata.setCurrentqc(model.getQC());
                BuildProbeInfoDto(hospitalcode,equipmentno,
                        CurrentProbeInfoEnum.CURRENTQC.getInstrumentConfigId(),model.getQC(),
                        CurrentProbeInfoEnum.CURRENTQC.getProbeEName());
                WarningMqModel warningMqModel90 = showModelUtils.procWarnModel(model.getQC(), monitorinstrument, model.getNowTime(), 7, "电量");
                list.add(warningMqModel90);
                if (RegularUtil.checkContainsNumbers(model.getTEMP2()) && RegularUtil.checkContainsNumbers(model.getTEMP3())) {
                    //两个值全部正常，才能计算差值
                    Double a = new Double(model.getTEMP2());
                    Double b = new Double(model.getTEMP3());
                    double abs = Math.abs(a - b);
                    BigDecimal bg = new BigDecimal(abs);
                    double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    String format = df.format(f1);
                    monitorequipmentlastdata.setCurrenttemperaturediff(format);
                    BuildProbeInfoDto(hospitalcode,equipmentno,
                            CurrentProbeInfoEnum.CURRENTTEMPERATUREDIFF.getInstrumentConfigId(),format,
                            CurrentProbeInfoEnum.CURRENTTEMPERATUREDIFF.getProbeEName());
                    WarningMqModel warningMqModel900 = showModelUtils.procWarnModel(format, monitorinstrument, model.getNowTime(), 26, "温差");
                    list.add(warningMqModel900);
                }
                break;
            case "9c":
                //温度
                if (StringUtils.isNotEmpty(model.getTEMP())) {
                    //首先确定有值，然后确定是非双探头才存值
                    monitorequipmentlastdata.setCurrenttemperature(model.getTEMP());
                    BuildProbeInfoDto(hospitalcode,equipmentno,
                            CurrentProbeInfoEnum.CURRENT_TEMPERATURE.getInstrumentConfigId(),model.getTEMP(),
                            CurrentProbeInfoEnum.CURRENT_TEMPERATURE.getProbeEName());
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(model.getTEMP(), monitorinstrument, model.getNowTime(), 4, "温度");
                    if (RegularUtil.checkContainsNumbers(model.getTEMP())) {
                        if (StringUtils.isNotEmpty(model.getTEMP2())) {
                            if (!RegularUtil.checkContainsNumbers(model.getTEMP4()) || Math.abs(new Double(model.getTEMP()) - new Double(model.getTEMP4())) > 3) {
                                monitorequipmentlastdata.setCurrenttemperature(ProbeOutlier.VALUE_IS_INVALID.getCode());
                                BuildProbeInfoDto(hospitalcode,equipmentno,
                                        CurrentProbeInfoEnum.CURRENT_TEMPERATURE.getInstrumentConfigId(),ProbeOutlier.VALUE_IS_INVALID.getCode(),
                                        CurrentProbeInfoEnum.CURRENT_TEMPERATURE.getProbeEName());
                            }
                            warningMqModel97.setCurrentData1(model.getTEMP4());
                        }
                    }
                    list.add(warningMqModel97);
                }
                monitorequipmentlastdata.setCurrenttemperature2(model.getTEMP2());// 室温
                BuildProbeInfoDto(hospitalcode,equipmentno,
                        CurrentProbeInfoEnum.CURRENTTEMPERATURE2.getInstrumentConfigId(),model.getTEMP2(),
                        CurrentProbeInfoEnum.CURRENTTEMPERATURE2.getProbeEName());
                monitorequipmentlastdata.setCurrenttemperature3(model.getTEMP3()); // 壁温
                BuildProbeInfoDto(hospitalcode,equipmentno,
                        CurrentProbeInfoEnum.CURRENTTEMPERATURE3.getInstrumentConfigId(),model.getTEMP3(),
                        CurrentProbeInfoEnum.CURRENTTEMPERATURE3.getProbeEName());
                monitorequipmentlastdata.setCurrentqcl(model.getQC());
                BuildProbeInfoDto(hospitalcode,equipmentno,
                        CurrentProbeInfoEnum.CURRENTQCL.getInstrumentConfigId(),model.getQC(),
                        CurrentProbeInfoEnum.CURRENTQCL.getProbeEName());
                WarningMqModel warningMqModel99 = showModelUtils.procWarnModel(model.getQC(), monitorinstrument, model.getNowTime(), 7, "锁电量");
                list.add(warningMqModel99);
                if (RegularUtil.checkContainsNumbers(model.getTEMP2()) && RegularUtil.checkContainsNumbers(model.getTEMP3())) {
                    //两个值全部正常，才能计算差值
                    Double a = new Double(model.getTEMP2());
                    Double b = new Double(model.getTEMP3());
                    double abs = Math.abs(a - b);
                    BigDecimal bg = new BigDecimal(abs);
                    double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    String format = df.format(f1);
                    monitorequipmentlastdata.setCurrenttemperaturediff(format);
                    BuildProbeInfoDto(hospitalcode,equipmentno,
                            CurrentProbeInfoEnum.CURRENTTEMPERATUREDIFF.getInstrumentConfigId(),format,
                            CurrentProbeInfoEnum.CURRENTTEMPERATUREDIFF.getProbeEName());
                    WarningMqModel warningMqModel900 = showModelUtils.procWarnModel(format, monitorinstrument, model.getNowTime(), 26, "温差");
                    list.add(warningMqModel900);
                }
                break;
            case "97":
                // 判断是否存在温度探头
                if (StringUtils.isNotEmpty(model.getTEMP())) {
                    monitorequipmentlastdata.setCurrenttemperature(model.getTEMP());
                    BuildProbeInfoDto(hospitalcode,equipmentno,
                            CurrentProbeInfoEnum.CURRENT_TEMPERATURE.getInstrumentConfigId(),model.getTEMP(),
                            CurrentProbeInfoEnum.CURRENT_TEMPERATURE.getProbeEName());
                    //执行报警服务
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(model.getTEMP(), monitorinstrument, model.getNowTime(), 4, "温度");
                    list.add(warningMqModel97);
                }
                // CO2
                if (StringUtils.isNotEmpty(model.getCO2())) {
                    monitorequipmentlastdata.setCurrentcarbondioxide(model.getCO2());
                    BuildProbeInfoDto(hospitalcode,equipmentno,
                            CurrentProbeInfoEnum.CURRENTCARBONDIOXIDE.getInstrumentConfigId(),model.getCO2(),
                            CurrentProbeInfoEnum.CURRENTCARBONDIOXIDE.getProbeEName());
                    WarningMqModel warningCO2Model = showModelUtils.procWarnModel(model.getCO2(), monitorinstrument, model.getNowTime(), 1, "CO2");
                    list.add(warningCO2Model);
                }
                // O2
                if (StringUtils.isNotEmpty(model.getO2())) {
                    monitorequipmentlastdata.setCurrento2(model.getO2());
                    BuildProbeInfoDto(hospitalcode,equipmentno,
                            CurrentProbeInfoEnum.CURRENTO2.getInstrumentConfigId(),model.getO2(),
                            CurrentProbeInfoEnum.CURRENTO2.getProbeEName());
                    WarningMqModel warningO2Model = showModelUtils.procWarnModel(model.getO2(), monitorinstrument, model.getNowTime(), 2, "O2");
                    list.add(warningO2Model);
                }
                // 湿度
                if (StringUtils.isNotEmpty(model.getRH())) {
                    monitorequipmentlastdata.setCurrenthumidity(model.getRH());
                    BuildProbeInfoDto(hospitalcode,equipmentno,
                            CurrentProbeInfoEnum.CURRENTHUMIDITY.getInstrumentConfigId(),model.getRH(),
                            CurrentProbeInfoEnum.CURRENTHUMIDITY.getProbeEName());
                    WarningMqModel warningRhModel = showModelUtils.procWarnModel(model.getRH(), monitorinstrument, model.getNowTime(), 5, "湿度");
                    list.add(warningRhModel);
                }
                // PM5
                if (StringUtils.isNotEmpty(model.getPM50())) {
                    monitorequipmentlastdata.setCurrentpm5(model.getPM50());
                    BuildProbeInfoDto(hospitalcode,equipmentno,
                            CurrentProbeInfoEnum.CURRENTPM5.getInstrumentConfigId(),model.getPM50(),
                            CurrentProbeInfoEnum.CURRENTPM5.getProbeEName());
                    WarningMqModel warningPm5Model = showModelUtils.procWarnModel(model.getPM50(), monitorinstrument, model.getNowTime(), 27, "PM5");
                    list.add(warningPm5Model);
                }
                // PM0.5
                if (StringUtils.isNotEmpty(model.getPM05())) {
                    monitorequipmentlastdata.setCurrentpm05(model.getPM05());
                    BuildProbeInfoDto(hospitalcode,equipmentno,
                            CurrentProbeInfoEnum.CURRENTPM05.getInstrumentConfigId(),model.getPM05(),
                            CurrentProbeInfoEnum.CURRENTPM05.getProbeEName());
                    WarningMqModel warningPM05Model = showModelUtils.procWarnModel(model.getPM05(), monitorinstrument, model.getNowTime(), 28, "PM05");
                    list.add(warningPM05Model);
                }
                // 甲醛
                if (StringUtils.isNotEmpty(model.getOX())) {
                    monitorequipmentlastdata.setCurrentformaldehyde(model.getOX());
                    BuildProbeInfoDto(hospitalcode,equipmentno,
                            CurrentProbeInfoEnum.CURRENTFORMALDEHYDE.getInstrumentConfigId(),model.getOX(),
                            CurrentProbeInfoEnum.CURRENTFORMALDEHYDE.getProbeEName());
                    WarningMqModel warningOxModel = showModelUtils.procWarnModel(model.getOX(), monitorinstrument, model.getNowTime(), 12, "甲醛");
                    list.add(warningOxModel);
                }
                break;
            case "98":
                String airflow2 = model.getAirflow();
                if (StringUtils.isNotEmpty(airflow2)) {
                    monitorequipmentlastdata.setCurrentairflow1(airflow2);
                    //只有airFlow 是 I M O才报警生成报警模型
                    if (StringUtils.equalsAnyIgnoreCase(airflow2, "I", "M", "O")) {
                        WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(airflow2, monitorinstrument, model.getNowTime(), 25, "气流");
                        list.add(warningMqModel97);
                    }
                }
                break;
            case "8a":// 互创桌面培养箱
            case "99":// 澳门镜湖 有线十舱室培养箱
                //一路温度
                if (StringUtils.isNotEmpty(model.getTEMP())) {
                    showModelUtils.Temp1(list, monitorinstrument, model, equipmentno, model.getTEMP(), model.getNowTime(), monitorequipmentlastdata);
                }
                if (StringUtils.isNotEmpty(model.getTEMP2())) {
                    showModelUtils.Temp2(list, monitorinstrument, model, equipmentno, model.getTEMP2(), model.getNowTime(), monitorequipmentlastdata);
                }
                if (StringUtils.isNotEmpty(model.getTEMP3())) {
                    showModelUtils.Temp3(list, monitorinstrument, model, equipmentno, model.getTEMP3(), model.getNowTime(), monitorequipmentlastdata);
                }
                if (StringUtils.isNotEmpty(model.getTEMP4())) {
                    showModelUtils.Temp4(list, monitorinstrument, model, equipmentno, model.getTEMP4(), model.getNowTime(), monitorequipmentlastdata);
                }
                if (StringUtils.isNotEmpty(model.getTEMP5())) {
                    showModelUtils.Temp5(list, monitorinstrument, model, equipmentno, model.getTEMP5(), model.getNowTime(), monitorequipmentlastdata);
                }
                if (StringUtils.isNotEmpty(model.getTEMP6())) {
                    showModelUtils.Temp6(list, monitorinstrument, model, equipmentno, model.getTEMP6(), model.getNowTime(), monitorequipmentlastdata);
                }
                if (StringUtils.isNotEmpty(model.getTEMP7())) {
                    showModelUtils.Temp7(list, monitorinstrument, model, equipmentno, model.getTEMP7(), model.getNowTime(), monitorequipmentlastdata);
                }
                if (StringUtils.isNotEmpty(model.getTEMP8())) {
                    showModelUtils.Temp8(list, monitorinstrument, model, equipmentno, model.getTEMP8(), model.getNowTime(), monitorequipmentlastdata);
                }
                if (StringUtils.isNotEmpty(model.getTEMP9())) {
                    showModelUtils.Temp9(list, monitorinstrument, model, equipmentno, model.getTEMP9(), model.getNowTime(), monitorequipmentlastdata);
                }
                //十路温度
                if (StringUtils.isNotEmpty(model.getTEMP10())) {
                    showModelUtils.Temp10(list, monitorinstrument, model, equipmentno, model.getTEMP10(), model.getNowTime(), monitorequipmentlastdata);
                }
                // CO2
                if (StringUtils.isNotEmpty(model.getCO2())) {
                    showModelUtils.CO2(list, monitorinstrument, model, equipmentno, model.getCO2(), model.getNowTime(), monitorequipmentlastdata);
                }

                // O2
                if (StringUtils.isNotEmpty(model.getO2())) {
                    showModelUtils.O2(list, monitorinstrument, model, equipmentno, model.getO2(), model.getNowTime(), monitorequipmentlastdata);
                }
                //流量1
                if (StringUtils.isNotEmpty(model.getPM05())) {
                    showModelUtils.PM05(list, monitorinstrument, model, equipmentno, model.getO2(), model.getNowTime(), monitorequipmentlastdata);
                }
                //流量2
                if (StringUtils.isNotEmpty(model.getPM50())) {
                    showModelUtils.PM5(list, monitorinstrument, model, equipmentno, model.getO2(), model.getNowTime(), monitorequipmentlastdata);
                }
                //气压1
                if (StringUtils.isNotEmpty(model.getRH())) {
                    showModelUtils.RH(list, monitorinstrument, model, equipmentno, model.getO2(), model.getNowTime(), monitorequipmentlastdata);
                }
                //气压2
                if (StringUtils.isNotEmpty(model.getOX())) {
                    showModelUtils.JQ(list, monitorinstrument, model, equipmentno, model.getO2(), model.getNowTime(), monitorequipmentlastdata);
                }
                break;
            case "9a":
                if (StringUtils.isNotEmpty(model.getTEMP())) {
                    // 左盖板温度
                    monitorequipmentlastdata.setCurrentleftcovertemperature(model.getTEMP());
                    //执行报警服务
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(model.getTEMP(), monitorinstrument, model.getNowTime(), 29, "左盖板温度");
                    list.add(warningMqModel97);

                }
                if (StringUtils.isNotEmpty(model.getTEMP2())) {
                    //左底板温度
                    monitorequipmentlastdata.setCurrentleftendtemperature(model.getTEMP2());
                    //执行报警服务
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(model.getTEMP2(), monitorinstrument, model.getNowTime(), 30, "左底板温度");
                    list.add(warningMqModel97);
                }
                if (StringUtils.isNotEmpty(model.getTEMP3())) {
                    //左气流
                    monitorequipmentlastdata.setCurrentleftairflow(model.getTEMP3());
                    //执行报警服务
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(model.getTEMP3(), monitorinstrument, model.getNowTime(), 31, "左气流");
                    list.add(warningMqModel97);
                }
                if (StringUtils.isNotEmpty(model.getTEMP4())) {
                    //右盖板温度
                    monitorequipmentlastdata.setCurrentrightcovertemperature(model.getTEMP4());
                    //执行报警服务
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(model.getTEMP4(), monitorinstrument, model.getNowTime(), 32, "右盖板温度");
                    list.add(warningMqModel97);
                }
                if (StringUtils.isNotEmpty(model.getTEMP5())) {
                    //右底板温度
                    monitorequipmentlastdata.setCurrentrightendtemperature(model.getTEMP5());
                    //执行报警服务
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(model.getTEMP5(), monitorinstrument, model.getNowTime(), 33, "右底板温度");
                    list.add(warningMqModel97);
                }
                if (StringUtils.isNotEmpty(model.getTEMP6())) {
                    //右气流
                    monitorequipmentlastdata.setCurrentrightairflow(model.getTEMP6());
                    //执行报警服务
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(model.getTEMP6(), monitorinstrument, model.getNowTime(), 34, "右气流");
                    list.add(warningMqModel97);
                }
                break;
            case "9b":
                /**
                 * 双路温度电量查询应答，命令 ID：0x9B（MT200M 项目）
                 */
                String sn2 = model.getSN();
                String proSn = sn2.substring(0, 4);
                if (StringUtils.isNotEmpty(model.getTEMP())) {
                    //老版本mt200m报警以及温度
                    monitorequipmentlastdata.setCurrenttemperature(model.getTEMP());
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(model.getTEMP(), monitorinstrument, model.getNowTime(), 4, "温度");
                    if (Integer.parseInt(proSn) < 2031) {
                        if (RegularUtil.checkContainsNumbers(model.getTEMP())) {
                            if (StringUtils.isNotEmpty(model.getTEMP2())) {
                                if (!RegularUtil.checkContainsNumbers(model.getTEMP2()) || Math.abs(new Double(model.getTEMP()) - new Double(model.getTEMP2())) > 3) {
                                    monitorequipmentlastdata.setCurrenttemperature(ProbeOutlier.VALUE_IS_INVALID.getCode());
                                }
                                warningMqModel97.setCurrentData1(model.getTEMP2());
                            }
                        }
                        list.add(warningMqModel97);
                    } else {
                        //温度一温度二均小于-197°认为值无效
                        String temp = model.getTEMP();
                        String temp2 = model.getTEMP2();
                        if (RegularUtil.checkContainsNumbers(temp)) {
                            BigDecimal bigDecimal = new BigDecimal(temp);
                            double v1 = bigDecimal.doubleValue();
                            if (v1 < -200.0) {
                                monitorequipmentlastdata.setCurrenttemperature(ProbeOutlier.VALUE_IS_INVALID.getCode());
                            }
                        }
                        monitorequipmentlastdata.setCurrenttemperature2(model.getTEMP2());
                        if (RegularUtil.checkContainsNumbers(temp2)) {
                            BigDecimal bigDecimal = new BigDecimal(temp2);
                            double v1 = bigDecimal.doubleValue();
                            if (v1 < -200.0) {
                                monitorequipmentlastdata.setCurrenttemperature2(ProbeOutlier.VALUE_IS_INVALID.getCode());
                            }
                            warningMqModel97.setCurrentData1(model.getTEMP2());
                        }
                        list.add(warningMqModel97);
                    }
                }
                if (StringUtils.isNotEmpty(model.getQC()) && !StringUtils.equals(model.getQC(), "0")) {
                    monitorequipmentlastdata.setCurrentqc(model.getQC());
                    WarningMqModel warningMqModel98 = showModelUtils.procWarnModel(model.getQC(), monitorinstrument, model.getNowTime(), 7, "电量");
                    list.add(warningMqModel98);
                }
                break;
            case "9f":
                if (StringUtils.isNotEmpty(model.getTEMP())) {
                    // 左盖板温度
                    monitorequipmentlastdata.setCurrentleftcovertemperature(model.getTEMP());
                    //执行报警服务
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(model.getTEMP(), monitorinstrument, model.getNowTime(), 29, "左舱室顶部温度");
                    list.add(warningMqModel97);
                }
                if (StringUtils.isNotEmpty(model.getTEMP2())) {
                    //左底板温度
                    monitorequipmentlastdata.setCurrentrightcovertemperature(model.getTEMP2());
                    //执行报警服务
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(model.getTEMP2(), monitorinstrument, model.getNowTime(), 30, "右舱室顶部温度");
                    list.add(warningMqModel97);
                }
                if (StringUtils.isNotEmpty(model.getTEMP3())) {
                    //左气流
                    monitorequipmentlastdata.setCurrentleftendtemperature(model.getTEMP3());
                    //执行报警服务
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(model.getTEMP3(), monitorinstrument, model.getNowTime(), 32, "左舱室底部温度");
                    list.add(warningMqModel97);
                }
                if (StringUtils.isNotEmpty(model.getTEMP4())) {
                    //右盖板温度
                    monitorequipmentlastdata.setCurrentrightendtemperature(model.getTEMP4());
                    //执行报警服务
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(model.getTEMP4(), monitorinstrument, model.getNowTime(), 33, "右舱室底部温度");
                    list.add(warningMqModel97);
                }
                if (StringUtils.isNotEmpty(model.getTEMP5())) {
                    //右底板温度
                    monitorequipmentlastdata.setCurrenttemperature(model.getTEMP5());
                    //执行报警服务
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(model.getTEMP5(), monitorinstrument, model.getNowTime(), 4, "温度");
                    list.add(warningMqModel97);
                }
                if (StringUtils.isNotEmpty(model.getTEMP6())) {
                    //右气流
                    monitorequipmentlastdata.setCurrentairflow1(model.getTEMP6());
                    //执行报警服务
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(model.getTEMP6(), monitorinstrument, model.getNowTime(), 25, "气流");
                    list.add(warningMqModel97);
                }
                break;
            case "a1":
                // 舱室一到舱室10
                String temp1 = model.getTEMP();
                if (StringUtils.isNotEmpty(temp1)) {
                    monitorequipmentlastdata.setCurrenttemperature1(temp1);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(temp1, monitorinstrument, model.getNowTime(), 13, "舱室1温度");
                    list.add(warningMqModel97);
                }
                String temp2 = model.getTEMP2();
                if (StringUtils.isNotEmpty(temp2)) {
                    monitorequipmentlastdata.setCurrenttemperature2(temp2);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(temp2, monitorinstrument, model.getNowTime(), 14, "舱室2温度");
                    list.add(warningMqModel97);
                }
                String temp3 = model.getTEMP3();
                if (StringUtils.isNotEmpty(temp3)) {
                    monitorequipmentlastdata.setCurrenttemperature3(temp3);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(temp3, monitorinstrument, model.getNowTime(), 15, "舱室3温度");
                    list.add(warningMqModel97);
                }
                String temp4 = model.getTEMP4();
                if (StringUtils.isNotEmpty(temp4)) {
                    monitorequipmentlastdata.setCurrenttemperature4(temp4);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(temp4, monitorinstrument, model.getNowTime(), 16, "舱室4温度");
                    list.add(warningMqModel97);
                }
                String temp5 = model.getTEMP5();
                if (StringUtils.isNotEmpty(temp5)) {
                    monitorequipmentlastdata.setCurrenttemperature5(temp5);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(temp5, monitorinstrument, model.getNowTime(), 17, "舱室5温度");
                    list.add(warningMqModel97);
                }
                String temp6 = model.getTEMP6();
                if (StringUtils.isNotEmpty(temp6)) {
                    monitorequipmentlastdata.setCurrenttemperature6(temp6);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(temp6, monitorinstrument, model.getNowTime(), 18, "舱室6温度");
                    list.add(warningMqModel97);
                }
                String temp7 = model.getTEMP7();
                if (StringUtils.isNotEmpty(temp7)) {
                    monitorequipmentlastdata.setCurrenttemperature7(temp7);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(temp7, monitorinstrument, model.getNowTime(), 19, "舱室7温度");
                    list.add(warningMqModel97);
                }
                String temp8 = model.getTEMP8();
                if (StringUtils.isNotEmpty(temp8)) {
                    monitorequipmentlastdata.setCurrenttemperature8(temp8);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(temp8, monitorinstrument, model.getNowTime(), 20, "舱室8温度");
                    list.add(warningMqModel97);
                }
                break;
            case "a2":
                String temp9 = model.getTEMP9();
                if (StringUtils.isNotEmpty(temp9)) {
                    monitorequipmentlastdata.setCurrenttemperature9(temp9);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(temp9, monitorinstrument, model.getNowTime(), 21, "舱室9温度");
                    list.add(warningMqModel97);
                }
                String temp10 = model.getTEMP10();
                if (StringUtils.isNotEmpty(temp10)) {
                    monitorequipmentlastdata.setCurrenttemperature10(temp10);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(temp10, monitorinstrument, model.getNowTime(), 22, "舱室10温度");
                    list.add(warningMqModel97);
                }
                String o22 = model.getO2();
                if (StringUtils.isNotEmpty(o22)) {
                    monitorequipmentlastdata.setCurrento2(o22);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(o22, monitorinstrument, model.getNowTime(), 2, "O2浓度");
                    list.add(warningMqModel97);
                }
                String n2 = model.getN2();
                if (StringUtils.isNotEmpty(n2)) {
                    monitorequipmentlastdata.setCurrentn2(n2);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(n2, monitorinstrument, model.getNowTime(), 36, "N2压力");
                    list.add(warningMqModel97);
                }
                String co21 = model.getCO2();
                if (StringUtils.isNotEmpty(co21)) {
                    monitorequipmentlastdata.setCurrentcarbondioxide(co21);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(co21, monitorinstrument, model.getNowTime(), 1, "CO2浓度");
                    list.add(warningMqModel97);
                }
                //压力
                String press1 = model.getPRESS();
                if (StringUtils.isNotEmpty(press1)) {
                    monitorequipmentlastdata.setCurrentairflow(press1);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(press1, monitorinstrument, model.getNowTime(), 6, "CO2压力");
                    list.add(warningMqModel97);
                }
                //气流
                String airflow1 = model.getAirflow();
                if (StringUtils.isNotEmpty(airflow1)) {
                    monitorequipmentlastdata.setCurrentairflow1(airflow1);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(airflow1, monitorinstrument, model.getNowTime(), 25, "气体流量");
                    list.add(warningMqModel97);
                }
                break;
            case "a3":
                String leftCompartmentTemp = model.getLeftCompartmentTemp();
                if (StringUtils.isNotEmpty(leftCompartmentTemp)) {
                    //对数据做非空判断
                    monitorequipmentlastdata.setCurrentlefttemperature(leftCompartmentTemp);
                    //生成报警模型，到MSCT处理
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(leftCompartmentTemp, monitorinstrument, model.getNowTime(), 23, "左舱室温度");
                    list.add(warningMqModel97);
                }
                String leftCompartmentFlow = model.getLeftCompartmentFlow();
                if (StringUtils.isNotEmpty(leftCompartmentFlow)) {
                    monitorequipmentlastdata.setCurrentleftairflow(leftCompartmentFlow);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(leftCompartmentFlow, monitorinstrument, model.getNowTime(), 31, "左舱室流量");
                    list.add(warningMqModel97);
                }
                String leftCompartmentHumidity = model.getLeftCompartmentHumidity();
                if (StringUtils.isNotEmpty(leftCompartmentHumidity)) {
                    monitorequipmentlastdata.setLeftCompartmentHumidity(leftCompartmentHumidity);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(leftCompartmentHumidity, monitorinstrument, model.getNowTime(), 37, "左舱室湿度");
                    list.add(warningMqModel97);
                }
                String rightCompartmentTemp = model.getRightCompartmentTemp();
                if (StringUtils.isNotEmpty(rightCompartmentTemp)) {
                    monitorequipmentlastdata.setCurrentrigthtemperature(rightCompartmentTemp);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(rightCompartmentTemp, monitorinstrument, model.getNowTime(), 24, "右舱室温度");
                    list.add(warningMqModel97);
                }
                String rightCompartmentFlow = model.getRightCompartmentFlow();
                if (StringUtils.isNotEmpty(rightCompartmentFlow)) {
                    monitorequipmentlastdata.setCurrentrightairflow(rightCompartmentFlow);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(rightCompartmentFlow, monitorinstrument, model.getNowTime(), 34, "右舱室流量");
                    list.add(warningMqModel97);
                }
                String rightCompartmentHumidity = model.getRightCompartmentHumidity();
                if (StringUtils.isNotEmpty(rightCompartmentHumidity)) {
                    monitorequipmentlastdata.setRightCompartmentHumidity(rightCompartmentHumidity);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(rightCompartmentHumidity, monitorinstrument, model.getNowTime(), 38, "右舱室湿度");
                    list.add(warningMqModel97);
                }
                break;
            case "a4":
                String ups1 = model.getUPS();
                if (StringUtils.isNotEmpty(ups1)) {
                    monitorequipmentlastdata.setCurrentups(ups1);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(ups1, monitorinstrument, model.getNowTime(), 10, "适配器供电状态");
                    list.add(warningMqModel97);
                }
                String voltage = model.getVoltage();
                if (StringUtils.isNotEmpty(voltage)) {
                    monitorequipmentlastdata.setVoltage(voltage);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(voltage, monitorinstrument, model.getNowTime(), 39, "电池电压");
                    list.add(warningMqModel97);
                }
                break;
            case "a5":
                String temp = model.getTEMP();
                if (StringUtils.isNotEmpty(temp)) {
                    monitorequipmentlastdata.setCurrenttemperature(temp);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(temp, monitorinstrument, model.getNowTime(), 4, "温度");
                    list.add(warningMqModel97);
                }
                String o21 = model.getO2();
                if (StringUtils.isNotEmpty(o21)) {
                    monitorequipmentlastdata.setCurrento2(o21);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(o21, monitorinstrument, model.getNowTime(), 2, "O2浓度百分比");
                    list.add(warningMqModel97);
                }
                String co2 = model.getCO2();
                if (StringUtils.isNotEmpty(co2)) {
                    monitorequipmentlastdata.setCurrentcarbondioxide(co2);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(co2, monitorinstrument, model.getNowTime(), 1, "CO2浓度百分比");
                    list.add(warningMqModel97);
                }
                String rh = model.getRH();
                if (StringUtils.isNotEmpty(rh)) {
                    monitorequipmentlastdata.setCurrenthumidity(rh);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(rh, monitorinstrument, model.getNowTime(), 5, "湿度百分比");
                    list.add(warningMqModel97);
                }
                break;
            case "a6":
                String temp11 = model.getTEMP();
                if (StringUtils.isNotEmpty(temp11)) {
                    monitorequipmentlastdata.setCurrenttemperature1(temp11);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(temp11, monitorinstrument, model.getNowTime(), 13, "舱室1温度");
                    list.add(warningMqModel97);
                }
                String temp12 = model.getTEMP2();
                if (StringUtils.isNotEmpty(temp12)) {
                    monitorequipmentlastdata.setCurrenttemperature2(temp12);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(temp12, monitorinstrument, model.getNowTime(), 14, "舱室2温度");
                    list.add(warningMqModel97);
                }
                String temp13 = model.getTEMP3();
                if (StringUtils.isNotEmpty(temp13)) {
                    monitorequipmentlastdata.setCurrenttemperature3(temp13);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(temp13, monitorinstrument, model.getNowTime(), 15, "舱室3温度");
                    list.add(warningMqModel97);
                }
                String temp14 = model.getTEMP4();
                if (StringUtils.isNotEmpty(temp14)) {
                    monitorequipmentlastdata.setCurrenttemperature4(temp14);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(temp14, monitorinstrument, model.getNowTime(), 16, "舱室4温度");
                    list.add(warningMqModel97);
                }
                String temp15 = model.getTEMP5();
                if (StringUtils.isNotEmpty(temp15)) {
                    monitorequipmentlastdata.setCurrenttemperature5(temp15);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(temp15, monitorinstrument, model.getNowTime(), 17, "舱室5温度");
                    list.add(warningMqModel97);
                }
                String temp16 = model.getTEMP6();
                if (StringUtils.isNotEmpty(temp16)) {
                    monitorequipmentlastdata.setCurrenttemperature6(temp16);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(temp16, monitorinstrument, model.getNowTime(), 18, "舱室6温度");
                    list.add(warningMqModel97);
                }
                String o23 = model.getO2();
                if (StringUtils.isNotEmpty(o23)) {
                    monitorequipmentlastdata.setCurrento2(o23);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(o23, monitorinstrument, model.getNowTime(), 2, "O2浓度百分比");
                    list.add(warningMqModel97);
                }
                String co22 = model.getCO2();
                if (StringUtils.isNotEmpty(co22)) {
                    monitorequipmentlastdata.setCurrentcarbondioxide(co22);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(co22, monitorinstrument, model.getNowTime(), 1, "CO2浓度百分比");
                    list.add(warningMqModel97);
                }
                break;
            case "a8":
                String temp17 = model.getTEMP();
                if (StringUtils.isNotEmpty(temp17)) {
                    monitorequipmentlastdata.setCurrenttemperature(temp17);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(temp17, monitorinstrument, model.getNowTime(), 4, "温度");
                    list.add(warningMqModel97);
                }
                String co23 = model.getCO2();
                if (StringUtils.isNotEmpty(co23)) {
                    monitorequipmentlastdata.setCurrentcarbondioxide(co23);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(co23, monitorinstrument, model.getNowTime(), 1, "CO2");
                    list.add(warningMqModel97);
                }
                String o24 = model.getO2();
                if (StringUtils.isNotEmpty(o24)) {
                    monitorequipmentlastdata.setCurrento2(o24);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(o24, monitorinstrument, model.getNowTime(), 2, "O2");
                    list.add(warningMqModel97);
                }
                String rh1 = model.getRH();
                if (StringUtils.isNotEmpty(rh1)) {
                    monitorequipmentlastdata.setCurrenthumidity(rh1);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(rh1, monitorinstrument, model.getNowTime(), 5, "湿度");
                    list.add(warningMqModel97);
                }
                String press2 = model.getPRESS();
                if (StringUtils.isNotEmpty(press2)) {
                    monitorequipmentlastdata.setCurrentairflow(press2);
                    WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(press2, monitorinstrument, model.getNowTime(), 6, "压力");
                    list.add(warningMqModel97);
                }
                String pm252 = model.getPM25();
                if (StringUtils.isNotEmpty(pm252)) {
                    monitorequipmentlastdata.setCurrentpm25(pm252);
                    WarningMqModel warningMqModel1 = showModelUtils.procWarnModel(pm252, monitorinstrument, model.getNowTime(), 8, "PM2.5");
                    list.add(warningMqModel1);
                }
                String pm102 = model.getPM10();
                if (StringUtils.isNotEmpty(pm102)) {
                    monitorequipmentlastdata.setCurrentpm10(pm102);
                    WarningMqModel warningMqModel1 = showModelUtils.procWarnModel(pm102, monitorinstrument, model.getNowTime(), 9, "PM10");
                    list.add(warningMqModel1);
                }
                String ox = model.getOX();
                if (StringUtils.isNotEmpty(ox)) {
                    monitorequipmentlastdata.setCurrentformaldehyde(ox);
                    WarningMqModel warningMqModel1 = showModelUtils.procWarnModel(ox, monitorinstrument, model.getNowTime(), 12, "甲醛");
                    list.add(warningMqModel1);
                }
                String voc2 = model.getVOC();
                if (StringUtils.isNotEmpty(voc2)) {
                    monitorequipmentlastdata.setCurrentvoc(voc2);
                    WarningMqModel warningMqModel1 = showModelUtils.procWarnModel(voc2, monitorinstrument, model.getNowTime(), 3, "空气质量");
                    list.add(warningMqModel1);
                }
                break;
            case "aa":
                String current = model.getCurrent();
                if (StringUtils.isNotEmpty(current)) {
                    monitorequipmentlastdata.setQccurrent(current);
                    WarningMqModel warningMqModel1 = showModelUtils.procWarnModel(current, monitorinstrument, model.getNowTime(), 40, "电流");
                    list.add(warningMqModel1);
                }
                String voltage1 = model.getVoltage();
                if (StringUtils.isNotEmpty(voltage1)) {
                    monitorequipmentlastdata.setVoltage(voltage1);
                    WarningMqModel warningMqModel1 = showModelUtils.procWarnModel(voltage1, monitorinstrument, model.getNowTime(), 39, "电压");
                    list.add(warningMqModel1);
                }
                String power = model.getPower();
                if (StringUtils.isNotEmpty(power)) {
                    monitorequipmentlastdata.setPower(power);
                    WarningMqModel warningMqModel1 = showModelUtils.procWarnModel(power, monitorinstrument, model.getNowTime(), 41, "功率");
                    list.add(warningMqModel1);
                }
                String qc = model.getQC();
                if (StringUtils.isNotEmpty(qc)) {
                    WarningMqModel warningMqModel1 = showModelUtils.procWarnModel(model.getQC(), monitorinstrument, model.getNowTime(), 7, "电量");
                    monitorequipmentlastdata.setCurrentqc(qc);
                    list.add(warningMqModel1);
                }
                break;
            case "ab":
                monitorequipmentlastdata.setModel(model.getModel());
                String probe1model = model.getProbe1model();
                String probe1data = model.getProbe1data();
                String probe2model = model.getProbe2model();
                String probe2data = model.getProbe2data();
                String probe3model = model.getProbe3model();
                String probe3data = model.getProbe3data();
                String co24 = model.getCO2();
                String o2 = model.getO2();
                String voc3 = model.getVOC();
                monitorequipmentlastdata.setProbe1data(probe1data);
                monitorequipmentlastdata.setProbe1model(probe1model);
                monitorequipmentlastdata.setProbe2data(probe2data);
                monitorequipmentlastdata.setProbe2model(probe2model);
                monitorequipmentlastdata.setProbe3data(probe3data);
                monitorequipmentlastdata.setProbe3model(probe3model);
                monitorequipmentlastdata.setCurrentcarbondioxide(co24);
                monitorequipmentlastdata.setCurrento2(o2);
                monitorequipmentlastdata.setCurrentvoc(voc3);
                WarningMqModel probeWaringModel1;
                if (StringUtils.equalsIgnoreCase(probe1model, ProbeOutlierMt310.THE_TEMPERATURE.getCode())){
                    probeWaringModel1 = showModelUtils.procWarnModel(probe1data, monitorinstrument, model.getNowTime(), 4, "温度");
                }else if (StringUtils.equalsIgnoreCase(probe1model, ProbeOutlierMt310.HUMIDITY.getCode())){
                    probeWaringModel1 = showModelUtils.procWarnModel(probe1data, monitorinstrument, model.getNowTime(), 5, "湿度");
                }else  if (StringUtils.equalsIgnoreCase(probe1model, ProbeOutlierMt310.OXYGEN_CONCENTRATIONS.getCode())){
                    probeWaringModel1 = showModelUtils.procWarnModel(probe1data, monitorinstrument, model.getNowTime(), 2, "氧气");
                }else {
                    probeWaringModel1 = showModelUtils.procWarnModel(probe1data, monitorinstrument, model.getNowTime(), 1, "二氧化碳");
                }
                list.add(probeWaringModel1);

                WarningMqModel probeWaringModel2;
                if (StringUtils.equalsIgnoreCase(probe2model, ProbeOutlierMt310.THE_TEMPERATURE.getCode())){
                    probeWaringModel2 = showModelUtils.procWarnModel(probe2data, monitorinstrument, model.getNowTime(), 4, "温度");
                }else if (StringUtils.equalsIgnoreCase(probe2model, ProbeOutlierMt310.HUMIDITY.getCode())){
                    probeWaringModel2 = showModelUtils.procWarnModel(probe2data, monitorinstrument, model.getNowTime(), 5, "湿度");
                }else  if (StringUtils.equalsIgnoreCase(probe2model, ProbeOutlierMt310.OXYGEN_CONCENTRATIONS.getCode())){
                    probeWaringModel2 = showModelUtils.procWarnModel(probe2data, monitorinstrument, model.getNowTime(), 2, "氧气");
                }else {
                    probeWaringModel2 = showModelUtils.procWarnModel(probe2data, monitorinstrument, model.getNowTime(), 1, "二氧化碳");
                }
                list.add(probeWaringModel2);

                WarningMqModel probeWaringModel3;
                if (StringUtils.equalsIgnoreCase(probe3model, ProbeOutlierMt310.THE_TEMPERATURE.getCode())){
                    probeWaringModel3 = showModelUtils.procWarnModel(probe3data, monitorinstrument, model.getNowTime(), 4, "温度");
                }else if (StringUtils.equalsIgnoreCase(probe3model, ProbeOutlierMt310.HUMIDITY.getCode())){
                    probeWaringModel3 = showModelUtils.procWarnModel(probe3data, monitorinstrument, model.getNowTime(), 5, "湿度");
                }else  if (StringUtils.equalsIgnoreCase(probe3model, ProbeOutlierMt310.OXYGEN_CONCENTRATIONS.getCode())){
                    probeWaringModel3 = showModelUtils.procWarnModel(probe3data, monitorinstrument, model.getNowTime(), 2, "氧气");
                }else {
                    probeWaringModel3 = showModelUtils.procWarnModel(probe3data, monitorinstrument, model.getNowTime(), 1, "二氧化碳");
                }
                list.add(probeWaringModel3);
                list.add(showModelUtils.procWarnModel(o2, monitorinstrument, model.getNowTime(), 43, "氧气"));
                list.add(showModelUtils.procWarnModel(co24, monitorinstrument, model.getNowTime(), 42, "二氧化碳"));
                list.add(showModelUtils.procWarnModel(voc3, monitorinstrument, model.getNowTime(), 3, "VOC"));
                break;
            default:
                break;
        }
        if (!ObjectUtils.isEmpty(monitorequipmentlastdata)) {
            //从缓存中取数据
            lastDataService.saveLastData(monitorequipmentlastdata, equipmentno, monitorinstrument.getHospitalcode(),cmdid,sn);
        }
        return list;
    }

    public String chu(Double a, String b) {
        return new BigDecimal(a).divide(new BigDecimal(b), 2, BigDecimal.ROUND_HALF_UP).toString();
    }

    public void  BuildProbeInfoDto(String hospitalCode, String equipmentNo, int instrumentConfigId ,String value, String probeEName){
        ProbeInfoDto probeInfoDto =  new ProbeInfoDto();
        probeInfoDto.setInputTime(new Date());
        probeInfoDto.setInstrumentConfigId(instrumentConfigId);
        probeInfoDto.setHospitalCode(hospitalCode);
        probeInfoDto.setEquipmentNo(equipmentNo);
        probeInfoDto.setValue(value);
        probeInfoDto.setProbeEName(probeEName);
        //调用redis缓存
    }
}



