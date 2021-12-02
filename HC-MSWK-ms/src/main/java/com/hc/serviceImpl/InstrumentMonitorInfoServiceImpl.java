package com.hc.serviceImpl;

import com.hc.bean.RecordTime;
import com.hc.bean.ShowModel;
import com.hc.bean.WarningMqModel;
import com.hc.config.RedisTemplateUtil;
import com.hc.entity.*;
import com.hc.model.RequestModel.InstrumentInfoModel;
import com.hc.msctservice.MsctService;
import com.hc.my.common.core.bean.ParamaterModel;
import com.hc.service.CurrentDataService;
import com.hc.service.InstrumentMonitorInfoService;
import com.hc.service.LastDataService;
import com.hc.utils.JsonUtil;
import com.hc.utils.ShowModelUtils;
import com.hc.utils.TimeHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class InstrumentMonitorInfoServiceImpl implements InstrumentMonitorInfoService {


    private static final Logger log = LoggerFactory.getLogger(InstrumentMonitorInfoServiceImpl.class);
    DecimalFormat df = new DecimalFormat("######0.00");

    @Autowired
    private LastDataService lastDataService;
    @Autowired
    private MsctService msctService;
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;
    @Autowired
    private CurrentDataService currentDataService;
    @Autowired
    private ShowModelUtils showModelUtils;

    @Override
    public List<WarningMqModel> save(ParamaterModel model, Monitorinstrument monitorinstrument) {
        HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
        //命令id
        Date time = model.getNowTime();
        Monitorequipmentlastdata monitorequipmentlastdata = new Monitorequipmentlastdata();
        ShowModel showModel = new ShowModel();
        String equipmentno = monitorinstrument.getEquipmentno();
        String hospitalcode = monitorinstrument.getHospitalcode();
        if (StringUtils.isEmpty(equipmentno) || StringUtils.isEmpty(monitorinstrument.getInstrumentno())) {
            log.info("不存在数据：" + JsonUtil.toJson(monitorinstrument));
            return null;
        }
        /**
         *   数据插入到redis中，做超时设置： （需要做超时报警医院从redis里面获取）
         */

        String sn = model.getSN();
        RecordTime recordTime = new RecordTime();
        recordTime.setInputdatetime(time);
        List<WarningMqModel> list = new ArrayList<>();
        String cmdid = model.getCmdid();
        // 判断当前上传数据当前时间
        if (StringUtils.equals("8d", cmdid)) {
            //开关量问题：
            log.info("开关量时间计算：" + JsonUtil.toJson(model));
            String channel = monitorinstrument.getChannel();
            if (StringUtils.isEmpty(channel)) {
                log.info("非300和300LITE" + JsonUtil.toJson(model));
                if (objectObjectObjectHashOperations.hasKey(model.getSN(), model.getCmdid())) {
                    String o = (String) objectObjectObjectHashOperations.get(model.getSN(), model.getCmdid());
                    if (StringUtils.isEmpty(o)) {
                        objectObjectObjectHashOperations.put(model.getSN(), model.getCmdid(), JsonUtil.toJson(recordTime));
                    } else {
                        RecordTime recordTime1 = JsonUtil.toBean(o, RecordTime.class);
                        //时间比较
                        double datePoorMs = TimeHelper.getDatePoorMs(time, recordTime1.getInputdatetime());
                        if (datePoorMs < 55) {
                            log.info("当前上传频率异常，抛弃设备" + JsonUtil.toJson(model));
                            return null;
                        } else {
                            objectObjectObjectHashOperations.put(model.getSN(), model.getCmdid(), JsonUtil.toJson(recordTime));
                        }
                    }

                } else {
                    objectObjectObjectHashOperations.put(model.getSN(), model.getCmdid(), JsonUtil.toJson(recordTime));
                }
            } else {
                if (objectObjectObjectHashOperations.hasKey(model.getSN(), model.getCmdid() + ":" + channel)) {
                    String o = (String) objectObjectObjectHashOperations.get(model.getSN(), model.getCmdid() + ":" + channel);
                    if (StringUtils.isEmpty(o)) {
                        objectObjectObjectHashOperations.put(model.getSN(), model.getCmdid() + ":" + channel, JsonUtil.toJson(recordTime));
                    } else {
                        RecordTime recordTime1 = JsonUtil.toBean(o, RecordTime.class);
                        //时间比较
                        double datePoorMs = TimeHelper.getDatePoorMs(time, recordTime1.getInputdatetime());
                        if (datePoorMs < 55) {
                            log.info("当前上传频率异常，抛弃设备" + JsonUtil.toJson(model));
                            return null;
                        } else {
                            objectObjectObjectHashOperations.put(model.getSN(), model.getCmdid() + ":" + channel, JsonUtil.toJson(recordTime));
                        }
                    }

                } else {
                    objectObjectObjectHashOperations.put(model.getSN(), model.getCmdid() + ":" + channel, JsonUtil.toJson(recordTime));
                }
            }

        } else if (!StringUtils.equals(cmdid, "9a")) {
            if (objectObjectObjectHashOperations.hasKey(model.getSN(), model.getCmdid())) {
                String o = (String) objectObjectObjectHashOperations.get(model.getSN(), model.getCmdid());
                if (StringUtils.isEmpty(o)) {
                    objectObjectObjectHashOperations.put(model.getSN(), model.getCmdid(), JsonUtil.toJson(recordTime));
                } else {
                    RecordTime recordTime1 = JsonUtil.toBean(o, RecordTime.class);
                    //时间比较
                    double datePoorMs = TimeHelper.getDatePoorMs(time, recordTime1.getInputdatetime());
                    if (datePoorMs < 55) {
                        log.info("当前上传频率异常，抛弃设备" + JsonUtil.toJson(model));
                        return null;
                    } else {
                        objectObjectObjectHashOperations.put(model.getSN(), model.getCmdid(), JsonUtil.toJson(recordTime));
                    }
                }

            } else {
                objectObjectObjectHashOperations.put(model.getSN(), model.getCmdid(), JsonUtil.toJson(recordTime));
            }
        }
        try {
            String substring = sn.substring(4, 6);
            showModelUtils.dataTimeOut(equipmentno);
            switch (model.getCmdid()) {
                case "85":
                    //获取 探头e类型id 用同步缓存(条件：温度 电量    sn号获取instypeid )
                    //根据监控参数类型编号、探头编号、探头类型编号查询监控参数编号\

                    String o = (String) objectObjectObjectHashOperations.get("insprobe"+hospitalcode, monitorinstrument.getInstrumentno() + ":4");
                    if (StringUtils.equals("04", substring)) {
                        if (StringUtils.isNotEmpty(model.getTEMP2()) && StringUtils.isNotEmpty(o)) {
                            String calibration = showModelUtils.calibration(o, model.getTEMP2());
                            // 判断是否存在温度探头
                            //  currentDataService.getCurrentData(model.getTEMP(),monitorinstrument,"TEMP",model.getNowTime());
                            showModel.setEquipmentno(equipmentno);
                            showModel.setData(calibration);
                            showModel.setUnit("温度");
                            showModel.setInputdatetime(time);
                            objectObjectObjectHashOperations.put("TEMP", equipmentno, JsonUtil.toJson(showModel));
                            monitorequipmentlastdata.setCurrenttemperature(calibration);
                            try {

                            } catch (Exception e) {
                                log.error("cmdid:" + model.getCmdid() + " SN:" + sn + "温度插入失败：" + e.getMessage() + "数据：" + JsonUtil.toJson(model));
                            }
                            //   log.info("执行插入temp:设备sn号   " + sn + "插入的模型:" + JsonUtil.toJson(monitortemperaturerecord));
                            //执行报警服务
                            WarningMqModel warningMqModel = showModelUtils.procWarnModel(calibration, monitorinstrument, model.getNowTime(), 4, "温度");
                            list.add(warningMqModel);
                        } else {
                            if (StringUtils.equals("1832120013", sn) && StringUtils.isEmpty(o)) {
                                try {
                                    log.info("进入拨打程序");
                                    msctService.test2("18108674918", "瑞迪斯存储探头值失效");
                                    msctService.test2("17786499503", "瑞迪斯存储探头值失效");
                                } catch (Exception e) {
                                    log.error("redis拨打电话异常：" + e.getMessage());
                                }
                            }
                            log.error("当前设备探头未同步至redis缓存：" + JsonUtil.toJson(model));
                        }
                    } else {
                        if (StringUtils.isNotEmpty(model.getTEMP()) && StringUtils.isNotEmpty(o)) {
                            String calibration = showModelUtils.calibration(o, model.getTEMP());
                            if (!StringUtils.equalsAny(calibration, "0", "0.00", "0.0")) {
                                showModel.setEquipmentno(equipmentno);
                                showModel.setData(calibration);
                                showModel.setUnit("温度");
                                showModel.setInputdatetime(time);
                                objectObjectObjectHashOperations.put("TEMP", equipmentno, JsonUtil.toJson(showModel));
                                monitorequipmentlastdata.setCurrenttemperature(calibration);
                                try {
                                    // monitorTempDao.saveAndFlush(monitortemperaturerecord);
                                } catch (Exception e) {
                                    log.error("cmdid:" + model.getCmdid() + " SN:" + sn + "温度插入失败：" + e.getMessage() + "数据：" + JsonUtil.toJson(model));
                                }
                                //   log.info("执行插入temp:设备sn号   " + sn + "插入的模型:" + JsonUtil.toJson(monitortemperaturerecord));
                                //执行报警服务
                                WarningMqModel warningMqModel = showModelUtils.procWarnModel(calibration, monitorinstrument, model.getNowTime(), 4, "温度");
                                list.add(warningMqModel);
                            }
                        }
                    }
                    if (StringUtils.isNotEmpty(model.getQC()) && !StringUtils.equals(model.getQC(), "0")) {
                        if (StringUtils.endsWithAny(substring, "02", "18")) {
                            //液氮罐锁电量
                            monitorequipmentlastdata.setCurrentqcl(model.getQC());
                        } else {
                            //常规液氮罐电量
                            monitorequipmentlastdata.setCurrentqc(model.getQC());
                        }

                        WarningMqModel warningMqModel = showModelUtils.procWarnModel(model.getQC(), monitorinstrument, model.getNowTime(), 7, "电量");
                        list.add(warningMqModel);
                    }
                    break;
                case "87":
                    // CO2 O2 二氧化碳氧气
                    String o1 = (String) objectObjectObjectHashOperations.get("insprobe"+hospitalcode, monitorinstrument.getInstrumentno() + ":1");
                    String o2 = (String) objectObjectObjectHashOperations.get("insprobe"+hospitalcode, monitorinstrument.getInstrumentno() + ":2");
                    if (StringUtils.isNotEmpty(model.getCO2()) && StringUtils.isNotEmpty(o1)) {
                        String calibration = showModelUtils.calibration(o1, model.getCO2());
                        showModel.setEquipmentno(equipmentno);
                        showModel.setData(calibration);
                        showModel.setUnit("CO2");
                        showModel.setInputdatetime(time);
                        objectObjectObjectHashOperations.put("CO2", equipmentno, JsonUtil.toJson(showModel));

                        monitorequipmentlastdata.setCurrentcarbondioxide(calibration);

                        WarningMqModel warningMqModel = showModelUtils.procWarnModel(calibration, monitorinstrument, model.getNowTime(), 1, "CO2");
                        list.add(warningMqModel);

                    }
                    if (StringUtils.isNotEmpty(model.getO2()) && StringUtils.isNotEmpty(o2)) {
                        String calibration = showModelUtils.calibration(o2, model.getO2());
                        showModel.setEquipmentno(equipmentno);
                        showModel.setData(calibration);
                        showModel.setUnit("O2");
                        showModel.setInputdatetime(time);
                        objectObjectObjectHashOperations.put("O2", equipmentno, JsonUtil.toJson(showModel));
                        //  Monitoro2record monitoro2record = new Monitoro2record();
                        monitorequipmentlastdata.setCurrento2(calibration);

                        WarningMqModel warningMqModel = showModelUtils.procWarnModel(calibration, monitorinstrument, model.getNowTime(), 2, "O2");
                        list.add(warningMqModel);
                    }
                    break;
                case "89":
                    //市电
                    log.info("市电：" + JsonUtil.toJson(model));
                    if (!StringUtils.isEmpty(model.getUPS())) {
                        String ups = "1"; // 表示市电异常
                        if ("3".equals(model.getUPS()) || "4".equals(model.getUPS())) {
                            ups = "0";//市电正常
                        }
                        //      currentDataService.getCurrentData(ups,monitorinstrument,"UPS",model.getNowTime());
                        showModel.setEquipmentno(equipmentno);
                        if (StringUtils.equals("1", ups)) {
                            showModel.setData("异常");
                        } else {
                            showModel.setData("正常");
                        }
                        showModel.setUnit("市电");
                        showModel.setInputdatetime(time);
                        objectObjectObjectHashOperations.put("UPS", equipmentno, JsonUtil.toJson(showModel));
                        Monitorupsrecord monitorupsrecord = new Monitorupsrecord();
                        monitorupsrecord.setEquipmentno(equipmentno);
                        monitorupsrecord.setInputdatetime(time);
                        monitorupsrecord.setUps(ups);
                        monitorupsrecord.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
//                        try {
////                            monitorUpsDao.saveAndFlush(monitorupsrecord);
//                        } catch (Exception e) {
//                            log.error("cmdid:" + model.getCmdid() + " SN:" + sn + "市电插入失败：" + e.getMessage() + "数据：" + JsonUtil.toJson(model));
//                        }
                        monitorequipmentlastdata.setCurrentups(ups);
                        log.info("执行插入市电:设备sn号   " + sn + "插入的模型:" + JsonUtil.toJson(monitorupsrecord));
                        WarningMqModel warningMqModel = showModelUtils.procWarnModel(ups, monitorinstrument, model.getNowTime(), 10, "市电");
                        list.add(warningMqModel);
                    }
                    break;
                case "8d":
                    //开关门记录
                    //查询是否存在开门量（报警信息探头）
                    String dd = (String) objectObjectObjectHashOperations.get("insprobe"+hospitalcode, monitorinstrument.getInstrumentno() + ":11");
                    if (StringUtils.isNotEmpty(model.getDOORZ()) && StringUtils.isNotEmpty(dd)) {
                        String DOOR = "0";
                        if ("3".equals(model.getDOORZ()) || "1".equals(model.getDOORZ())) {
                            //表示关门
                            DOOR = "0";
                        } else {
                            DOOR = "1";
                            //表示开门
                        }
                        Monitordoorstaterecord monitordoorstaterecord = new Monitordoorstaterecord();
                        monitordoorstaterecord.setDoorstate(DOOR);
                        monitordoorstaterecord.setEquipmentno(equipmentno);
                        monitordoorstaterecord.setInputdatetime(time);
                        monitordoorstaterecord.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
//                        try {
//                            monitroDoorDao.saveAndFlush(monitordoorstaterecord);
//                        } catch (Exception e) {
//                            log.error("cmdid:" + model.getCmdid() + " SN:" + sn + "开关门记录插入失败：" + e.getMessage() + "数据：" + JsonUtil.toJson(model));
//                        }
                        monitorequipmentlastdata.setCurrentdoorstate(DOOR);
                        log.info("执行插入开关门:设备sn号   " + sn + "插入的模型:" + JsonUtil.toJson(monitordoorstaterecord));
                        WarningMqModel warningMqModel = showModelUtils.procWarnModel(DOOR, monitorinstrument, model.getNowTime(), 11, "DOOR");
                        list.add(warningMqModel);
                    } else {
                        return null;
                    }
                    break;
                case "8e":
                    // 3 :VOC 8: PM2.5 9: PM10 12: 甲醛 6: 压力   5:湿度
                    String voc = (String) objectObjectObjectHashOperations.get("insprobe"+hospitalcode, monitorinstrument.getInstrumentno() + ":3");
                    String pm25 = (String) objectObjectObjectHashOperations.get("insprobe"+hospitalcode, monitorinstrument.getInstrumentno() + ":8");
                    String pm10 = (String) objectObjectObjectHashOperations.get("insprobe"+hospitalcode, monitorinstrument.getInstrumentno() + ":9");
                    String jq = (String) objectObjectObjectHashOperations.get("insprobe"+hospitalcode, monitorinstrument.getInstrumentno() + ":12");
                    String yl = (String) objectObjectObjectHashOperations.get("insprobe"+hospitalcode, monitorinstrument.getInstrumentno() + ":6");
                    String sd = (String) objectObjectObjectHashOperations.get("insprobe"+hospitalcode, monitorinstrument.getInstrumentno() + ":5");
                    if (StringUtils.isNotEmpty(model.getVOC()) && StringUtils.isNotEmpty(voc)) {
                        String calibration = showModelUtils.calibration(voc, model.getVOC());
                        showModel.setEquipmentno(equipmentno);
                        showModel.setData(calibration);
                        showModel.setUnit("空气质量");
                        showModel.setInputdatetime(time);
                        objectObjectObjectHashOperations.put("VOC", equipmentno, JsonUtil.toJson(showModel));

                        monitorequipmentlastdata.setCurrentvoc(calibration);
                        //     log.info("执行插入环境VOC:设备sn号   " + sn + "插入的模型:" + JsonUtil.toJson(monitorvocrecord));
                        WarningMqModel warningMqModel = showModelUtils.procWarnModel(calibration, monitorinstrument, model.getNowTime(), 3, "空气质量");
                        list.add(warningMqModel);
                    }
                    if (StringUtils.isNotEmpty(model.getOX()) && StringUtils.isNotEmpty(jq)) {
                        String calibration = showModelUtils.calibration(jq, model.getOX());
                        showModel.setEquipmentno(equipmentno);
                        showModel.setData(calibration);
                        showModel.setUnit("甲醛");
                        showModel.setInputdatetime(time);
                        objectObjectObjectHashOperations.put("JQ", equipmentno, JsonUtil.toJson(showModel));
                        monitorequipmentlastdata.setCurrentformaldehyde(calibration);
                        //  log.info("执行插入环境甲醛:设备sn号   " + sn + "插入的模型:" + JsonUtil.toJson(jqrecord));
                        WarningMqModel warningMqModel = showModelUtils.procWarnModel(calibration, monitorinstrument, model.getNowTime(), 12, "甲醛");
                        list.add(warningMqModel);
                    }
                    if (StringUtils.isNotEmpty(model.getPM10()) && StringUtils.isNotEmpty(pm10)) {
                        String calibration = showModelUtils.calibration(pm10, model.getPM10());
                        showModel.setEquipmentno(equipmentno);
                        showModel.setData(calibration);
                        showModel.setUnit("PM10");
                        showModel.setInputdatetime(time);
                        objectObjectObjectHashOperations.put("PM10", equipmentno, JsonUtil.toJson(showModel));
                        monitorequipmentlastdata.setCurrentpm10(calibration);
                        //    log.info("执行插入环境PM2.5:设备sn号   " + sn + "插入的模型:" + JsonUtil.toJson(monitorpm10record));
                        WarningMqModel warningMqModel = showModelUtils.procWarnModel(calibration, monitorinstrument, model.getNowTime(), 9, "PM10");
                        list.add(warningMqModel);
                    }
                    if (StringUtils.isNotEmpty(model.getPM25()) && StringUtils.isNotEmpty(pm25)) {
                        String calibration = showModelUtils.calibration(pm25, model.getPM25());
                        showModel.setEquipmentno(equipmentno);
                        showModel.setData(calibration);
                        showModel.setUnit("PM2.5");
                        showModel.setInputdatetime(time);
                        objectObjectObjectHashOperations.put("PM25", equipmentno, JsonUtil.toJson(showModel));

                        monitorequipmentlastdata.setCurrentpm25(calibration);
                        //   log.info("执行插入环境PM10:设备sn号   " + sn + "插入的模型:" + JsonUtil.toJson(monitorpm25record));
                        WarningMqModel warningMqModel = showModelUtils.procWarnModel(calibration, monitorinstrument, model.getNowTime(), 8, "PM2.5");
                        list.add(warningMqModel);
                    }
                    if (StringUtils.isNotEmpty(model.getPRESS()) && StringUtils.isNotEmpty(yl)) {

                        String calibration = showModelUtils.calibration(yl, model.getPRESS());
                        if (!StringUtils.equalsAny(calibration, "0.0", "0.00", "0")) {
                            showModel.setEquipmentno(equipmentno);
                            showModel.setData(calibration);
                            showModel.setUnit("压力");
                            showModel.setInputdatetime(time);
                            objectObjectObjectHashOperations.put("PRESS", equipmentno, JsonUtil.toJson(showModel));
                            monitorequipmentlastdata.setCurrentairflow(calibration);
                            //         log.info("执行插入环境压力:设备sn号   " + sn + "插入的模型:" + JsonUtil.toJson(pressrecord));
                            WarningMqModel warningMqModel = showModelUtils.procWarnModel(calibration, monitorinstrument, model.getNowTime(), 6, "压力");
                            list.add(warningMqModel);
                        }
                    }
                    if (StringUtils.isNotEmpty(model.getRH()) && StringUtils.isNotEmpty(sd)) {
                        String calibration = showModelUtils.calibration(sd, model.getRH());
                        if (!StringUtils.equalsAny(calibration, "0", "0.0", "0.00")) {
                            showModel.setEquipmentno(equipmentno);
                            showModel.setData(calibration);
                            showModel.setUnit("湿度");
                            showModel.setInputdatetime(time);
                            objectObjectObjectHashOperations.put("RH", equipmentno, JsonUtil.toJson(showModel));

                            monitorequipmentlastdata.setCurrenthumidity(calibration);
                            //    log.info("执行插入环境湿度:设备sn号   " + sn + "插入的模型:" + JsonUtil.toJson(monitorhumidityrecord));
                            WarningMqModel warningMqModel = showModelUtils.procWarnModel(calibration, monitorinstrument, model.getNowTime(), 5, "湿度");
                            list.add(warningMqModel);
                        }
                    }
                    break;
                case "91":
                    // CO2 O2 温度
                    String o3 = (String) objectObjectObjectHashOperations.get("insprobe"+hospitalcode, monitorinstrument.getInstrumentno() + ":1");
                    String o4 = (String) objectObjectObjectHashOperations.get("insprobe"+hospitalcode, monitorinstrument.getInstrumentno() + ":2");
                    String o5 = (String) objectObjectObjectHashOperations.get("insprobe"+hospitalcode, monitorinstrument.getInstrumentno() + ":4");

                    if (StringUtils.isNotEmpty(model.getCO2()) && StringUtils.isNotEmpty(o3)) {
                        String calibration = showModelUtils.calibration(o3, model.getCO2());
                        showModel.setEquipmentno(equipmentno);
                        showModel.setData(calibration);
                        showModel.setUnit("CO2");
                        showModel.setInputdatetime(time);
                        objectObjectObjectHashOperations.put("CO2", equipmentno, JsonUtil.toJson(showModel));
                        //   Monitorcarbondioxiderecord monitorcarbondioxiderecord = new Monitorcarbondioxiderecord();
                        monitorequipmentlastdata.setCurrentcarbondioxide(calibration);
                        //    monitorcarbondioxiderecord.setCarbondioxide(model.getCO2());
                        log.info("91设备编号：" + equipmentno);
                        WarningMqModel warningMqModel = showModelUtils.procWarnModel(calibration, monitorinstrument, model.getNowTime(), 1, "CO2");
                        list.add(warningMqModel);

                    }
                    if (StringUtils.isNotEmpty(model.getO2()) && StringUtils.isNotEmpty(o4)) {
                        String calibration = showModelUtils.calibration(o4, model.getO2());
                        showModel.setEquipmentno(equipmentno);
                        showModel.setData(calibration);
                        showModel.setUnit("O2");
                        showModel.setInputdatetime(time);
                        objectObjectObjectHashOperations.put("O2", equipmentno, JsonUtil.toJson(showModel));
                        Monitoro2record monitoro2record = new Monitoro2record();
                        monitorequipmentlastdata.setCurrento2(calibration);
                        log.info("91设备编号：" + equipmentno);
                        log.info("执行插入o2:设备sn号   " + sn + "插入的模型:" + JsonUtil.toJson(monitoro2record));
                        WarningMqModel warningMqModel = showModelUtils.procWarnModel(calibration, monitorinstrument, model.getNowTime(), 2, "O2");
                        list.add(warningMqModel);
                    }
                    if (StringUtils.isNotEmpty(model.getTEMP()) && StringUtils.isNotEmpty(o5)) {
                        String calibration = showModelUtils.calibration(o5, model.getTEMP());
                        showModel.setEquipmentno(equipmentno);
                        showModel.setData(calibration);
                        showModel.setUnit("温度");
                        showModel.setInputdatetime(time);
                        objectObjectObjectHashOperations.put("TEMP", equipmentno, JsonUtil.toJson(showModel));
                        monitorequipmentlastdata.setCurrenttemperature(calibration);

                        WarningMqModel warningMqModel = showModelUtils.procWarnModel(calibration, monitorinstrument, model.getNowTime(), 4, "温度");
                        list.add(warningMqModel);
                    }
                    break;
                case "70":
                    //模拟500的温度
                    String o7 = (String) objectObjectObjectHashOperations.get("insprobe"+hospitalcode, monitorinstrument.getInstrumentno() + ":4");
                    if (StringUtils.isNotEmpty(o7) && StringUtils.isNotEmpty(model.getTEMP())) {
                        String calibration = showModelUtils.calibration(o7, model.getTEMP());
                        showModel.setEquipmentno(equipmentno);
                        showModel.setData(calibration);
                        showModel.setUnit("温度");
                        showModel.setInputdatetime(time);
                        objectObjectObjectHashOperations.put("TEMP", equipmentno, JsonUtil.toJson(showModel));
                        //  Monitortemperaturerecord monitortemperaturerecord = new Monitortemperaturerecord();
                        // 数据插入和报警服务这里是异步
                        monitorequipmentlastdata.setCurrenttemperature(calibration);

                        WarningMqModel warningMqModel = showModelUtils.procWarnModel(calibration, monitorinstrument, model.getNowTime(), 4, "温度");
                        list.add(warningMqModel);
                    }
                    break;
                case "71":
                    //模拟500的co2
                    String o6 = (String) objectObjectObjectHashOperations.get("insprobe"+hospitalcode, monitorinstrument.getInstrumentno() + ":1");
                    if (StringUtils.isNotEmpty(o6) && StringUtils.isNotEmpty(model.getCO2())) {
                        String calibration = showModelUtils.calibration(o6, model.getCO2());
                        showModel.setEquipmentno(equipmentno);
                        showModel.setData(calibration);
                        showModel.setUnit("CO2");
                        showModel.setInputdatetime(time);
                        objectObjectObjectHashOperations.put("CO2", equipmentno, JsonUtil.toJson(showModel));
                        //      Monitorcarbondioxiderecord monitorcarbondioxiderecord = new Monitorcarbondioxiderecord();
                        monitorequipmentlastdata.setCurrentcarbondioxide(calibration);

                        WarningMqModel warningMqModel1 = showModelUtils.procWarnModel(calibration, monitorinstrument, model.getNowTime(), 1, "CO2");
                        list.add(warningMqModel1);
                    }
                    break;
                case "72":
                    //模拟500的RH
                    showModel.setEquipmentno(equipmentno);
                    showModel.setData(model.getRH());
                    showModel.setUnit("湿度");
                    showModel.setInputdatetime(time);
                    objectObjectObjectHashOperations.put("RH", equipmentno, JsonUtil.toJson(showModel));
                    try {
                        monitorequipmentlastdata.setCurrenthumidity(model.getRH());
                        //   monitorRhDao.saveAndFlush(monitorhumidityrecord);
                    } catch (Exception e) {
                        log.error("cmdid:" + model.getCmdid() + " SN:" + sn + "湿度插入失败：" + e.getMessage() + "数据：" + JsonUtil.toJson(model));
                    }
                    //     log.info("执行插入环境湿度:设备sn号   " + sn + "插入的模型:" + JsonUtil.toJson(monitorhumidityrecord));
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
                    showModel.setEquipmentno(equipmentno);
                    showModel.setData(voc1);
                    showModel.setUnit("空气质量");
                    showModel.setInputdatetime(time);
                    objectObjectObjectHashOperations.put("VOC", equipmentno, JsonUtil.toJson(showModel));

                    monitorequipmentlastdata.setCurrentvoc(voc1);
                    //     log.info("执行插入环境VOC:设备sn号   " + sn + "插入的模型:" + JsonUtil.toJson(monitorvocrecord));
                    WarningMqModel warningMqModel3 = showModelUtils.procWarnModel(voc1, monitorinstrument, model.getNowTime(), 3, "空气质量");
                    list.add(warningMqModel3);
                    break;
                case "74":
                    //模拟500 PM2.5
                    showModel.setEquipmentno(equipmentno);
                    showModel.setData(model.getPM25());
                    showModel.setUnit("PM2.5");
                    showModel.setInputdatetime(time);
                    objectObjectObjectHashOperations.put("PM25", equipmentno, JsonUtil.toJson(showModel));
                    String pm251 = model.getPM25();
                    Double integer2 = Double.parseDouble(pm251);
                    if (integer2 < 0.1) {
                        integer2 = integer2 * 100;
                    }
                    monitorequipmentlastdata.setCurrentpm25(integer2.toString());
                    //   log.info("执行插入环境PM10:设备sn号   " + sn + "插入的模型:" + JsonUtil.toJson(monitorpm25record));
                    WarningMqModel warningMqModel4 = showModelUtils.procWarnModel(integer2.toString(), monitorinstrument, model.getNowTime(), 8, "PM2.5");
                    list.add(warningMqModel4);
                    break;
                case "75":
                    //模拟500 O2
                    String o8 = (String) objectObjectObjectHashOperations.get("insprobe"+hospitalcode, monitorinstrument.getInstrumentno() + ":2");
                    if (StringUtils.isNotEmpty(o8)) {
                        showModel.setEquipmentno(equipmentno);
                        showModel.setData(model.getO2());
                        showModel.setUnit("O2");
                        showModel.setInputdatetime(time);
                        objectObjectObjectHashOperations.put("O2", equipmentno, JsonUtil.toJson(showModel));
                        monitorequipmentlastdata.setCurrento2(model.getO2());
                        WarningMqModel warningMqModel5 = showModelUtils.procWarnModel(model.getO2(), monitorinstrument, model.getNowTime(), 2, "O2");
                        list.add(warningMqModel5);
                    }
                    break;
                case "76":
                    //模拟500 QC
                    monitorequipmentlastdata.setCurrentqc(model.getQC());
//               备sn号 " + sn + "插入的模型:" + JsonUtil.toJson(qcrecord));
                    WarningMqModel warningMqModel6 = showModelUtils.procWarnModel(model.getQC(), monitorinstrument, model.getNowTime(), 7, "电量");
                    list.add(warningMqModel6);
                    break;
                case "77":
                    //模拟500 UPS
                    String ups = "0";
                    if ("3".equals(model.getUPS()) || "4".equals(model.getUPS())) {
                        ups = "1";
                    }
                    currentDataService.getCurrentData(ups, monitorinstrument, "UPS", model.getNowTime());
                    Monitorupsrecord monitorupsrecord = new Monitorupsrecord();
                    monitorupsrecord.setEquipmentno(equipmentno);
                    monitorupsrecord.setInputdatetime(time);
                    monitorupsrecord.setUps(ups);
                    monitorupsrecord.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
                    monitorequipmentlastdata.setCurrentups(ups);
                    log.info("执行插入市电:设备sn号   " + sn + "插入的模型:" + JsonUtil.toJson(monitorupsrecord));
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
                        Monitordoorstaterecord monitordoorstaterecord = new Monitordoorstaterecord();
                        monitordoorstaterecord.setDoorstate(DOOR);
                        monitordoorstaterecord.setEquipmentno(equipmentno);
                        monitordoorstaterecord.setInputdatetime(time);
                        monitordoorstaterecord.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
                        monitorequipmentlastdata.setCurrentdoorstate(DOOR);
                        log.info("执行插入开关门:设备sn号   " + sn + "插入的模型:" + JsonUtil.toJson(monitordoorstaterecord));
                        WarningMqModel warningMqModel8 = showModelUtils.procWarnModel(DOOR, monitorinstrument, model.getNowTime(), 11, "DOOR");
                        list.add(warningMqModel8);
                    }
                    break;
                case "79":
                    //模拟500 OX
                    showModel.setEquipmentno(equipmentno);
                    showModel.setData(model.getOX());
                    showModel.setUnit("甲醛");
                    showModel.setInputdatetime(time);
                    objectObjectObjectHashOperations.put("JQ", equipmentno, JsonUtil.toJson(showModel));
                    monitorequipmentlastdata.setCurrentformaldehyde(model.getOX());
                    WarningMqModel warningMqModel9 = showModelUtils.procWarnModel(model.getOX(), monitorinstrument, model.getNowTime(), 12, "甲醛");
                    list.add(warningMqModel9);
                    break;
                case "7a":
                    String pm101 = model.getPM10();
                    Double v = Double.parseDouble(pm101);
                    if (v < 0.1) {
                        v = v * 100;
                    }
                    try {
                        monitorequipmentlastdata.setCurrentpm10(v.toString());
                    } catch (Exception e) {
                        log.error("cmdid:" + model.getCmdid() + " SN:" + sn + "PM10插入失败：" + e.getMessage() + "数据：" + JsonUtil.toJson(model));
                    }
                    WarningMqModel warningMqModel10 = showModelUtils.procWarnModel(v.toString(), monitorinstrument, model.getNowTime(), 9, "PM10");
                    list.add(warningMqModel10);
                    break;
                case "7b":
                    // 模拟500 压力  或者是培养箱气流
                    String press = model.getPRESS();
                    Double integer1 = Double.parseDouble(press);
                    if (integer1 < 100) {
                        integer1 = integer1 * 100;
                    }
                    monitorequipmentlastdata.setCurrentairflow(integer1.toString());
                    WarningMqModel warningMqModel11 = showModelUtils.procWarnModel(integer1.toString(), monitorinstrument, model.getNowTime(), 6, "压力");
                    list.add(warningMqModel11);
                    break;
                // COOK培养箱数据


                case "92":
                    String left = (String) objectObjectObjectHashOperations.get("insprobe"+hospitalcode, monitorinstrument.getInstrumentno() + ":23");
                    String right = (String) objectObjectObjectHashOperations.get("insprobe"+hospitalcode, monitorinstrument.getInstrumentno() + ":24");
                    String airflow = (String) objectObjectObjectHashOperations.get("insprobe"+hospitalcode, monitorinstrument.getInstrumentno() + ":25");
                    if (StringUtils.isNotEmpty(model.getTEMP()) && StringUtils.isNotEmpty(left)) {
                        // String calibration = calibration(left, model.getTEMP());
                        monitorequipmentlastdata.setCurrentlefttemperature(model.getTEMP());
                        WarningMqModel warningMqModel = showModelUtils.procWarnModel(model.getTEMP(), monitorinstrument, model.getNowTime(), 23, "左舱室温度");
                        list.add(warningMqModel);
                    }
                    if (StringUtils.isNotEmpty(model.getTEMP2()) && StringUtils.isNotEmpty(right)) {
                        //    calibration(right,)
                        monitorequipmentlastdata.setCurrentrigthtemperature(model.getTEMP2());
                        WarningMqModel warningMqModel = showModelUtils.procWarnModel(model.getTEMP2(), monitorinstrument, model.getNowTime(), 24, "右舱室温度");
                        list.add(warningMqModel);
                    }
                    if (StringUtils.isNotEmpty(model.getAirflow()) && StringUtils.isNotEmpty(airflow)) {
                        // 气流的值 除以100
                        monitorequipmentlastdata.setCurrentairflow1(model.getAirflow());
                        WarningMqModel warningMqModel = showModelUtils.procWarnModel(model.getAirflow(), monitorinstrument, model.getNowTime(), 25, "气流");
                        list.add(warningMqModel);
                    }
                    break;
                // C60培养箱
                case "93":
                    String yq = (String) objectObjectObjectHashOperations.get("insprobe"+hospitalcode, monitorinstrument.getInstrumentno() + ":2");
                    monitorequipmentlastdata.setCurrenttemperature(model.getTEMP());
                    WarningMqModel warningMqModel = showModelUtils.procWarnModel(model.getTEMP(), monitorinstrument, model.getNowTime(), 4, "温度");
                    list.add(warningMqModel);
                    if (StringUtils.isNotEmpty(model.getO2()) && StringUtils.isNotEmpty(yq)) {
                        monitorequipmentlastdata.setCurrento2(model.getO2());
                        WarningMqModel warningMqModel1 = showModelUtils.procWarnModel(model.getO2(), monitorinstrument, model.getNowTime(), 2, "O2");
                        list.add(warningMqModel1);
                    }
                    monitorequipmentlastdata.setCurrentcarbondioxide(model.getCO2());
                    WarningMqModel warningMqModel22 = showModelUtils.procWarnModel(model.getCO2(), monitorinstrument, model.getNowTime(), 1, "CO2");
                    list.add(warningMqModel22);
                    monitorequipmentlastdata.setCurrenthumidity(model.getRH());
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
                    List<String> listAb = new ArrayList<String>();
                    listAb.add("A");
                    listAb.add("B");
                    listAb.add("C");
                    listAb.add("D");

                    monitorequipmentlastdata.setCurrenttemperature(model.getTEMP());  //液氮罐温度
                    WarningMqModel warningMqMode90 = showModelUtils.procWarnModel(model.getTEMP(), monitorinstrument, model.getNowTime(), 4, "温度");
                    list.add(warningMqMode90);
                    monitorequipmentlastdata.setCurrenttemperature2(model.getTEMP2());// 室温
                    monitorequipmentlastdata.setCurrenttemperature3(model.getTEMP3()); // 壁温
                    monitorequipmentlastdata.setCurrentqc(model.getQC());
                    WarningMqModel warningMqModel90 = showModelUtils.procWarnModel(model.getQC(), monitorinstrument, model.getNowTime(), 7, "电量");
                    list.add(warningMqModel90);
                    if (!listAb.contains(model.getTEMP2()) && !listAb.contains(model.getTEMP3())) {
                        //两个值全部正常，才能计算差值
                        Double a = new Double(model.getTEMP2());
                        Double b = new Double(model.getTEMP3());
                        double abs = Math.abs(a - b);
                        BigDecimal bg = new BigDecimal(abs);
                        double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        String format = df.format(f1);
                        monitorequipmentlastdata.setCurrenttemperaturediff(format);
                        WarningMqModel warningMqModel900 = showModelUtils.procWarnModel(format, monitorinstrument, model.getNowTime(), 26, "温差");
                        list.add(warningMqModel900);
                    }
                    break;
                case "9c":
                    //温度
                    List<String> listAbs = new ArrayList<String>();
                    listAbs.add("A");
                    listAbs.add("B");
                    listAbs.add("C");
                    listAbs.add("D");
                    if (StringUtils.isNotEmpty(model.getTEMP())) {
                        //首先确定有值，然后确定是非双探头才存值
                        showModel.setEquipmentno(equipmentno);
                        showModel.setData(model.getTEMP());
                        showModel.setUnit("温度");
                        showModel.setInputdatetime(time);
                        objectObjectObjectHashOperations.put("TEMP", equipmentno, JsonUtil.toJson(showModel));
                        monitorequipmentlastdata.setCurrenttemperature(model.getTEMP());
                        WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(model.getTEMP(), monitorinstrument, model.getNowTime(), 4, "温度");
                        if (!StringUtils.equalsAny(model.getTEMP(), "A", "B", "C", "D", "E")) {
                            if (StringUtils.isNotEmpty(model.getTEMP2())) {
                                if (StringUtils.equalsAny(model.getTEMP4(), "A", "B", "C", "D", "E") || Math.abs(new Double(model.getTEMP()) - new Double(model.getTEMP4())) > 3) {
                                    monitorequipmentlastdata.setCurrenttemperature("C");
                                }
                                warningMqModel97.setCurrentData1(model.getTEMP4());
                            }
                        }
                        list.add(warningMqModel97);
                    }
                    monitorequipmentlastdata.setCurrenttemperature2(model.getTEMP2());// 室温
                    monitorequipmentlastdata.setCurrenttemperature3(model.getTEMP3()); // 壁温
                    monitorequipmentlastdata.setCurrentqcl(model.getQC());
                    WarningMqModel warningMqModel99 = showModelUtils.procWarnModel(model.getQC(), monitorinstrument, model.getNowTime(), 7, "锁电量");
                    list.add(warningMqModel99);
                    if (!listAbs.contains(model.getTEMP2()) && !listAbs.contains(model.getTEMP3())) {
                        //两个值全部正常，才能计算差值
                        Double a = new Double(model.getTEMP2());
                        Double b = new Double(model.getTEMP3());
                        double abs = Math.abs(a - b);
                        BigDecimal bg = new BigDecimal(abs);
                        double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        String format = df.format(f1);
                        monitorequipmentlastdata.setCurrenttemperaturediff(format);
                        WarningMqModel warningMqModel900 = showModelUtils.procWarnModel(format, monitorinstrument, model.getNowTime(), 26, "温差");
                        list.add(warningMqModel900);
                    }
                    break;
                case "97":
                    // 判断是否存在温度探头
                    if (StringUtils.isNotEmpty(model.getTEMP())) {
                        showModelUtils.Temp(list, objectObjectObjectHashOperations, monitorinstrument, model, showModel, equipmentno, model.getTEMP(), model.getNowTime(), monitorequipmentlastdata);
                    }

                    // CO2
                    if (StringUtils.isNotEmpty(model.getCO2())) {
                        showModelUtils.CO2(list, objectObjectObjectHashOperations, monitorinstrument, model, showModel, equipmentno, model.getCO2(), model.getNowTime(), monitorequipmentlastdata);
                    }

                    // O2
                    if (StringUtils.isNotEmpty(model.getO2())) {
                        showModelUtils.O2(list, objectObjectObjectHashOperations, monitorinstrument, model, showModel, equipmentno, model.getO2(), model.getNowTime(), monitorequipmentlastdata);
                    }

                    // 湿度
                    if (StringUtils.isNotEmpty(model.getRH())) {
                        showModelUtils.RH(list, objectObjectObjectHashOperations, monitorinstrument, model, showModel, equipmentno, model.getRH(), model.getNowTime(), monitorequipmentlastdata);
                    }

                    // PM5
                    if (StringUtils.isNotEmpty(model.getPM50())) {
                        showModelUtils.PM5(list, objectObjectObjectHashOperations, monitorinstrument, model, showModel, equipmentno, model.getPM50(), model.getNowTime(), monitorequipmentlastdata);
                    }
                    // PM0.5
                    if (StringUtils.isNotEmpty(model.getPM05())) {
                        showModelUtils.PM05(list, objectObjectObjectHashOperations, monitorinstrument, model, showModel, equipmentno, model.getPM05(), model.getNowTime(), monitorequipmentlastdata);
                    }
                    // 甲醛
                    if (StringUtils.isNotEmpty(model.getOX())) {
                        showModelUtils.JQ(list, objectObjectObjectHashOperations, monitorinstrument, model, showModel, equipmentno, model.getOX(), model.getNowTime(), monitorequipmentlastdata);
                    }
                    break;
                case "98":
                    String airflow2 = model.getAirflow();
                    if (StringUtils.isNotEmpty(airflow2)) {
                        monitorequipmentlastdata.setCurrentairflow1(airflow2);
                        ;
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
                        showModelUtils.Temp1(list, objectObjectObjectHashOperations, monitorinstrument, model, showModel, equipmentno, model.getTEMP(), model.getNowTime(), monitorequipmentlastdata);
                    }
                    if (StringUtils.isNotEmpty(model.getTEMP2())) {
                        showModelUtils.Temp2(list, objectObjectObjectHashOperations, monitorinstrument, model, showModel, equipmentno, model.getTEMP2(), model.getNowTime(), monitorequipmentlastdata);
                    }
                    if (StringUtils.isNotEmpty(model.getTEMP3())) {
                        showModelUtils.Temp3(list, objectObjectObjectHashOperations, monitorinstrument, model, showModel, equipmentno, model.getTEMP3(), model.getNowTime(), monitorequipmentlastdata);
                    }
                    if (StringUtils.isNotEmpty(model.getTEMP4())) {
                        showModelUtils.Temp4(list, objectObjectObjectHashOperations, monitorinstrument, model, showModel, equipmentno, model.getTEMP4(), model.getNowTime(), monitorequipmentlastdata);
                    }
                    if (StringUtils.isNotEmpty(model.getTEMP5())) {
                        showModelUtils.Temp5(list, objectObjectObjectHashOperations, monitorinstrument, model, showModel, equipmentno, model.getTEMP5(), model.getNowTime(), monitorequipmentlastdata);
                    }
                    if (StringUtils.isNotEmpty(model.getTEMP6())) {
                        showModelUtils.Temp6(list, objectObjectObjectHashOperations, monitorinstrument, model, showModel, equipmentno, model.getTEMP6(), model.getNowTime(), monitorequipmentlastdata);
                    }
                    if (StringUtils.isNotEmpty(model.getTEMP7())) {
                        showModelUtils.Temp7(list, objectObjectObjectHashOperations, monitorinstrument, model, showModel, equipmentno, model.getTEMP7(), model.getNowTime(), monitorequipmentlastdata);
                    }
                    if (StringUtils.isNotEmpty(model.getTEMP8())) {
                        showModelUtils.Temp8(list, objectObjectObjectHashOperations, monitorinstrument, model, showModel, equipmentno, model.getTEMP8(), model.getNowTime(), monitorequipmentlastdata);
                    }
                    if (StringUtils.isNotEmpty(model.getTEMP9())) {
                        showModelUtils.Temp9(list, objectObjectObjectHashOperations, monitorinstrument, model, showModel, equipmentno, model.getTEMP9(), model.getNowTime(), monitorequipmentlastdata);
                    }
                    //十路温度
                    if (StringUtils.isNotEmpty(model.getTEMP10())) {
                        showModelUtils.Temp10(list, objectObjectObjectHashOperations, monitorinstrument, model, showModel, equipmentno, model.getTEMP10(), model.getNowTime(), monitorequipmentlastdata);
                    }
                    // CO2
                    if (StringUtils.isNotEmpty(model.getCO2())) {
                        showModelUtils.CO2(list, objectObjectObjectHashOperations, monitorinstrument, model, showModel, equipmentno, model.getCO2(), model.getNowTime(), monitorequipmentlastdata);
                    }

                    // O2
                    if (StringUtils.isNotEmpty(model.getO2())) {
                        showModelUtils.O2(list, objectObjectObjectHashOperations, monitorinstrument, model, showModel, equipmentno, model.getO2(), model.getNowTime(), monitorequipmentlastdata);
                    }
                    //流量1
                    if (StringUtils.isNotEmpty(model.getPM05())) {
                        showModelUtils.PM05(list, objectObjectObjectHashOperations, monitorinstrument, model, showModel, equipmentno, model.getO2(), model.getNowTime(), monitorequipmentlastdata);
                    }
                    //流量2
                    if (StringUtils.isNotEmpty(model.getPM50())) {
                        showModelUtils.PM5(list, objectObjectObjectHashOperations, monitorinstrument, model, showModel, equipmentno, model.getO2(), model.getNowTime(), monitorequipmentlastdata);
                    }
                    //气压1
                    if (StringUtils.isNotEmpty(model.getRH())) {
                        showModelUtils.RH(list, objectObjectObjectHashOperations, monitorinstrument, model, showModel, equipmentno, model.getO2(), model.getNowTime(), monitorequipmentlastdata);
                    }
                    //气压2
                    if (StringUtils.isNotEmpty(model.getOX())) {
                        showModelUtils.JQ(list, objectObjectObjectHashOperations, monitorinstrument, model, showModel, equipmentno, model.getO2(), model.getNowTime(), monitorequipmentlastdata);
                    }
                    break;
                case "9a":
                    if (StringUtils.isNotEmpty(model.getTEMP())) {
                        // 左盖板温度
                        showModel.setEquipmentno(equipmentno);
                        showModel.setData(model.getTEMP());
                        showModel.setUnit("左盖板温度");
                        showModel.setInputdatetime(time);
                        objectObjectObjectHashOperations.put("TEMP", equipmentno, JsonUtil.toJson(showModel));
                        monitorequipmentlastdata.setCurrentleftcovertemperature(model.getTEMP());
                        //执行报警服务
                        WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(model.getTEMP(), monitorinstrument, model.getNowTime(), 29, "左盖板温度");
                        list.add(warningMqModel97);

                    }
                    if (StringUtils.isNotEmpty(model.getTEMP2())) {
                        //左底板温度
                        showModel.setEquipmentno(equipmentno);
                        showModel.setData(model.getTEMP2());
                        showModel.setUnit("左底板温度");
                        showModel.setInputdatetime(time);
                        objectObjectObjectHashOperations.put("TEMP", equipmentno, JsonUtil.toJson(showModel));
                        monitorequipmentlastdata.setCurrentleftendtemperature(model.getTEMP2());
                        //    log.info("执行插入temp:设备sn号   " + sn9n + "插入的模型:" + JsonUtil.toJson(monitortemperaturerecord));
                        //执行报警服务
                        WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(model.getTEMP2(), monitorinstrument, model.getNowTime(), 30, "左底板温度");
                        list.add(warningMqModel97);
                    }
                    if (StringUtils.isNotEmpty(model.getTEMP3())) {
                        //左气流
                        showModel.setEquipmentno(equipmentno);
                        showModel.setData(model.getTEMP3());
                        showModel.setUnit("左气流");
                        showModel.setInputdatetime(time);
                        objectObjectObjectHashOperations.put("气流", equipmentno, JsonUtil.toJson(showModel));
                        monitorequipmentlastdata.setCurrentleftairflow(model.getTEMP3());
                        //    log.info("执行插入temp:设备sn号   " + sn9n + "插入的模型:" + JsonUtil.toJson(monitortemperaturerecord));
                        //执行报警服务
                        WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(model.getTEMP3(), monitorinstrument, model.getNowTime(), 31, "左气流");
                        list.add(warningMqModel97);
                    }
                    if (StringUtils.isNotEmpty(model.getTEMP4())) {
                        //右盖板温度
                        showModel.setEquipmentno(equipmentno);
                        showModel.setData(model.getTEMP4());
                        showModel.setUnit("右盖板温度");
                        showModel.setInputdatetime(time);
                        objectObjectObjectHashOperations.put("TEMP", equipmentno, JsonUtil.toJson(showModel));
                        monitorequipmentlastdata.setCurrentrightcovertemperature(model.getTEMP4());
                        //    log.info("执行插入temp:设备sn号   " + sn9n + "插入的模型:" + JsonUtil.toJson(monitortemperaturerecord));
                        //执行报警服务
                        WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(model.getTEMP4(), monitorinstrument, model.getNowTime(), 32, "右盖板温度");
                        list.add(warningMqModel97);
                    }
                    if (StringUtils.isNotEmpty(model.getTEMP5())) {
                        //右底板温度
                        showModel.setEquipmentno(equipmentno);
                        showModel.setData(model.getTEMP5());
                        showModel.setUnit("右底板温度");
                        showModel.setInputdatetime(time);
                        objectObjectObjectHashOperations.put("TEMP", equipmentno, JsonUtil.toJson(showModel));
                        monitorequipmentlastdata.setCurrentrightendtemperature(model.getTEMP5());
                        //    log.info("执行插入temp:设备sn号   " + sn9n + "插入的模型:" + JsonUtil.toJson(monitortemperaturerecord));
                        //执行报警服务
                        WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(model.getTEMP5(), monitorinstrument, model.getNowTime(), 33, "右底板温度");
                        list.add(warningMqModel97);
                    }
                    if (StringUtils.isNotEmpty(model.getTEMP6())) {
                        //右气流
                        showModel.setEquipmentno(equipmentno);
                        showModel.setData(model.getTEMP6());
                        showModel.setUnit("右气流");
                        showModel.setInputdatetime(time);
                        objectObjectObjectHashOperations.put("气流", equipmentno, JsonUtil.toJson(showModel));
                        monitorequipmentlastdata.setCurrentrightairflow(model.getTEMP6());
                        //    log.info("执行插入temp:设备sn号   " + sn9n + "插入的模型:" + JsonUtil.toJson(monitortemperaturerecord));
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
                        showModel.setEquipmentno(equipmentno);
                        showModel.setData(model.getTEMP());
                        showModel.setUnit("温度");
                        showModel.setInputdatetime(time);
                        objectObjectObjectHashOperations.put("TEMP", equipmentno, JsonUtil.toJson(showModel));
                        monitorequipmentlastdata.setCurrenttemperature(model.getTEMP());
                        WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(model.getTEMP(), monitorinstrument, model.getNowTime(), 4, "温度");
                        if (Integer.parseInt(proSn) < 2031) {
                            if (!StringUtils.equalsAny(model.getTEMP(), "A", "B", "C", "D", "E")) {
                                if (StringUtils.isNotEmpty(model.getTEMP2())) {
                                    if (StringUtils.equalsAny(model.getTEMP2(), "A", "B", "C", "D", "E") || Math.abs(new Double(model.getTEMP()) - new Double(model.getTEMP2())) > 3) {
                                        monitorequipmentlastdata.setCurrenttemperature("C");
                                    }
                                    warningMqModel97.setCurrentData1(model.getTEMP2());
                                }
                            }
                            list.add(warningMqModel97);
                        } else {
                            //温度一温度二均小于-197°认为值无效
                            String temp = model.getTEMP();
                            String temp2 = model.getTEMP2();
                            if (!StringUtils.equalsAny(temp, "A", "B", "C", "D", "E")) {
                                BigDecimal bigDecimal = new BigDecimal(temp);
                                double v1 = bigDecimal.doubleValue();
                                if (v1 < -197.0) {
                                    monitorequipmentlastdata.setCurrenttemperature("C");
                                }
                            }
                            monitorequipmentlastdata.setCurrenttemperature2(model.getTEMP2());
                            if (!StringUtils.equalsAny(temp2, "A", "B", "C", "D", "E")) {
                                BigDecimal bigDecimal = new BigDecimal(temp2);
                                double v1 = bigDecimal.doubleValue();
                                if (v1 < -197.0) {
                                    monitorequipmentlastdata.setCurrenttemperature2("C");
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
                    String sn9n1 = model.getSN();
                    if (StringUtils.isNotEmpty(model.getTEMP())) {
                        // 左盖板温度
                        showModel.setEquipmentno(equipmentno);
                        showModel.setData(model.getTEMP());
                        showModel.setUnit("左舱室顶部温度");
                        showModel.setInputdatetime(time);
                        objectObjectObjectHashOperations.put("TEMP", equipmentno, JsonUtil.toJson(showModel));
                        Monitortemperaturerecord monitortemperaturerecord = new Monitortemperaturerecord();
                        monitorequipmentlastdata.setCurrentleftcovertemperature(model.getTEMP());
                        log.info("执行插入temp:设备sn号   " + sn9n1 + "插入的模型:" + JsonUtil.toJson(monitortemperaturerecord));
                        //执行报警服务
                        WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(model.getTEMP(), monitorinstrument, model.getNowTime(), 29, "左舱室顶部温度");
                        list.add(warningMqModel97);
                    }
                    if (StringUtils.isNotEmpty(model.getTEMP2())) {
                        //左底板温度
                        showModel.setEquipmentno(equipmentno);
                        showModel.setData(model.getTEMP2());
                        showModel.setUnit("右舱室顶部温度");
                        showModel.setInputdatetime(time);
                        objectObjectObjectHashOperations.put("TEMP", equipmentno, JsonUtil.toJson(showModel));
                        monitorequipmentlastdata.setCurrentrightcovertemperature(model.getTEMP2());
                        //    log.info("执行插入temp:设备sn号   " + sn9n1 + "插入的模型:" + JsonUtil.toJson(monitortemperaturerecord));
                        //执行报警服务
                        WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(model.getTEMP2(), monitorinstrument, model.getNowTime(), 30, "右舱室顶部温度");
                        list.add(warningMqModel97);
                    }
                    if (StringUtils.isNotEmpty(model.getTEMP3())) {
                        //左气流
                        showModel.setEquipmentno(equipmentno);
                        showModel.setData(model.getTEMP3());
                        showModel.setUnit("左舱室底部温度");
                        showModel.setInputdatetime(time);
                        objectObjectObjectHashOperations.put("TEMP", equipmentno, JsonUtil.toJson(showModel));
                        monitorequipmentlastdata.setCurrentleftendtemperature(model.getTEMP3());
                        //    log.info("执行插入temp:设备sn号   " + sn9n1 + "插入的模型:" + JsonUtil.toJson(monitortemperaturerecord));
                        //执行报警服务
                        WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(model.getTEMP3(), monitorinstrument, model.getNowTime(), 32, "左舱室底部温度");
                        list.add(warningMqModel97);
                    }
                    if (StringUtils.isNotEmpty(model.getTEMP4())) {
                        //右盖板温度
                        showModel.setEquipmentno(equipmentno);
                        showModel.setData(model.getTEMP4());
                        showModel.setUnit("右舱室底部温度");
                        showModel.setInputdatetime(time);
                        objectObjectObjectHashOperations.put("TEMP", equipmentno, JsonUtil.toJson(showModel));
                        monitorequipmentlastdata.setCurrentrightendtemperature(model.getTEMP4());
                        //    log.info("执行插入temp:设备sn号   " + sn9n1 + "插入的模型:" + JsonUtil.toJson(monitortemperaturerecord));
                        //执行报警服务
                        WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(model.getTEMP4(), monitorinstrument, model.getNowTime(), 33, "右舱室底部温度");
                        list.add(warningMqModel97);
                    }
                    if (StringUtils.isNotEmpty(model.getTEMP5())) {
                        //右底板温度
                        showModel.setEquipmentno(equipmentno);
                        showModel.setData(model.getTEMP5());
                        showModel.setUnit("加湿器温度");
                        showModel.setInputdatetime(time);
                        objectObjectObjectHashOperations.put("TEMP", equipmentno, JsonUtil.toJson(showModel));
                        monitorequipmentlastdata.setCurrenttemperature(model.getTEMP5());
                        //    log.info("执行插入temp:设备sn号   " + sn9n1 + "插入的模型:" + JsonUtil.toJson(monitortemperaturerecord));
                        //执行报警服务
                        WarningMqModel warningMqModel97 = showModelUtils.procWarnModel(model.getTEMP5(), monitorinstrument, model.getNowTime(), 4, "温度");
                        list.add(warningMqModel97);
                    }
                    if (StringUtils.isNotEmpty(model.getTEMP6())) {
                        //右气流
                        showModel.setEquipmentno(equipmentno);
                        showModel.setData(model.getTEMP6());
                        showModel.setUnit("气流");
                        showModel.setInputdatetime(time);
                        objectObjectObjectHashOperations.put("气流", equipmentno, JsonUtil.toJson(showModel));
                        monitorequipmentlastdata.setCurrentairflow1(model.getTEMP6());
                        //    log.info("执行插入temp:设备sn号   " + sn9n1 + "插入的模型:" + JsonUtil.toJson(monitortemperaturerecord));
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
                    if (StringUtils.isNotEmpty(current)){
                        monitorequipmentlastdata.setCurrent(current);
                        WarningMqModel warningMqModel1 = showModelUtils.procWarnModel(current, monitorinstrument, model.getNowTime(), 40, "电流");
                        list.add(warningMqModel1);
                    }
                    String voltage1 = model.getVoltage();
                    if (StringUtils.isNotEmpty(voltage1)){
                        monitorequipmentlastdata.setVoltage(voltage1);
                        WarningMqModel warningMqModel1 = showModelUtils.procWarnModel(voltage1, monitorinstrument, model.getNowTime(), 39, "电压");
                        list.add(warningMqModel1);
                    }
                    String power = model.getPower();
                    if (StringUtils.isNotEmpty(power)){
                        monitorequipmentlastdata.setPower(power);
                        WarningMqModel warningMqModel1 = showModelUtils.procWarnModel(power, monitorinstrument, model.getNowTime(), 41, "功率");
                        list.add(warningMqModel1);
                    }
                    String qc = model.getQC();
                    if (StringUtils.isNotEmpty(qc)){
                        WarningMqModel warningMqModel1 = showModelUtils.procWarnModel(model.getQC(), monitorinstrument, model.getNowTime(), 7, "电量");
                        list.add(warningMqModel1);
                    }
                    break;
                default:
                    break;
            }
            if (!ObjectUtils.isEmpty(monitorequipmentlastdata)) {
                //从缓存中取数据
                lastDataService.saveLastData(monitorequipmentlastdata, equipmentno, time, monitorinstrument.getHospitalcode());
            }
            return list;

        } catch (Exception e) {
            e.printStackTrace();
            log.error("cmdid:" + model.getCmdid() + " SN:" + sn + "数据插入失败：" + e.getMessage() + "数据：" + JsonUtil.toJson(model));
            return null;
        }
    }

    public String chu(Double a, String b) {
        return new BigDecimal(a).divide(new BigDecimal(b), 2, BigDecimal.ROUND_HALF_UP).toString();


    }
}



