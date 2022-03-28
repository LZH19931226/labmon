package com.hc.utils;

import com.hc.bean.ShowModel;
import com.hc.bean.WarningMqModel;
import com.hc.web.config.RedisTemplateUtil;
import com.hc.entity.Monitorequipmentlastdata;
import com.hc.entity.Monitorinstrument;
import com.hc.model.RequestModel.InstrumentInfoModel;
import com.hc.my.common.core.bean.ParamaterModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author LiuZhiHao
 * @date 2020/8/7 10:40
 * 描述:
 **/
@Component
public class ShowModelUtils {

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

    public WarningMqModel procWarnModel(String data, Monitorinstrument monitorinstrument, Date date, Integer instrumentconfigid, String unit) {
        WarningMqModel warningMqModel = new WarningMqModel();
        warningMqModel.setCurrrentData(data);
        warningMqModel.setDate(date);
        warningMqModel.setInstrumentconfigid(instrumentconfigid);
        warningMqModel.setMonitorinstrument(monitorinstrument);
        warningMqModel.setUnit(unit);
        return warningMqModel;
    }

    public void Temp(List<WarningMqModel> list, HashOperations<Object, Object, Object> objectObjectObjectHashOperations, Monitorinstrument monitorinstrument, ParamaterModel model, ShowModel showModel, String equipmentno, String data, Date time, Monitorequipmentlastdata monitorequipmentlastdata) {
        showModel.setEquipmentno(equipmentno);
        showModel.setData(data);
        showModel.setUnit("温度");
        showModel.setInputdatetime(time);
        objectObjectObjectHashOperations.put("TEMP", equipmentno, JsonUtil.toJson(showModel));
        //  Monitortemperaturerecord monitortemperaturerecord = new Monitortemperaturerecord();
        monitorequipmentlastdata.setCurrenttemperature(data);
        //   log.info("执行插入temp:设备sn号   " + sn + "插入的模型:" + JsonUtil.toJson(monitortemperaturerecord));
        //执行报警服务
        WarningMqModel warningMqModel97 = procWarnModel(model.getTEMP(), monitorinstrument, model.getNowTime(), 4, "温度");
        list.add(warningMqModel97);
    }

    public void CO2(List<WarningMqModel> list, HashOperations<Object, Object, Object> objectObjectObjectHashOperations, Monitorinstrument monitorinstrument, ParamaterModel model, ShowModel showModel, String equipmentno, String data, Date time, Monitorequipmentlastdata monitorequipmentlastdata) {
        showModel.setEquipmentno(equipmentno);
        showModel.setData(model.getCO2());
        showModel.setUnit("CO2");
        showModel.setInputdatetime(time);
        objectObjectObjectHashOperations.put("CO2", equipmentno, JsonUtil.toJson(showModel));
        //   Monitorcarbondioxiderecord monitorcarbondioxiderecord = new Monitorcarbondioxiderecord();
        monitorequipmentlastdata.setCurrentcarbondioxide(model.getCO2());
//        monitorcarbondioxiderecord.setCarbondioxide(model.getCO2());
//        monitorcarbondioxiderecord.setEquipmentno(equipmentno);
//        monitorcarbondioxiderecord.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
//        monitorcarbondioxiderecord.setInputdatetime(time);
//        monitorCO2Dao.saveAndFlush(monitorcarbondioxiderecord);
//        log.info("执行插入co2:设备sn号   " + sn + "插入的模型:" + JsonUtil.toJson(monitorcarbondioxiderecord));
        WarningMqModel warningMqModel = procWarnModel(model.getCO2(), monitorinstrument, model.getNowTime(), 1, "CO2");
        list.add(warningMqModel);
    }

    public void O2(List<WarningMqModel> list, HashOperations<Object, Object, Object> objectObjectObjectHashOperations, Monitorinstrument monitorinstrument, ParamaterModel model, ShowModel showModel, String equipmentno, String data, Date time, Monitorequipmentlastdata monitorequipmentlastdata) {
        showModel.setEquipmentno(equipmentno);
        showModel.setData(model.getO2());
        showModel.setUnit("O2");
        showModel.setInputdatetime(time);
        objectObjectObjectHashOperations.put("O2", equipmentno, JsonUtil.toJson(showModel));
        //  Monitoro2record monitoro2record = new Monitoro2record();
        monitorequipmentlastdata.setCurrento2(model.getO2());
//        monitoro2record.setEquipmentno(equipmentno);
//        monitoro2record.setInputdatetime(time);
//        monitoro2record.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
//        monitoro2record.setO2(model.getO2());
//        monitorO2Dao.saveAndFlush(monitoro2record);
//        log.info("执行插入o2:设备sn号   " + sn + "插入的模型:" + JsonUtil.toJson(monitoro2record));
        WarningMqModel warningMqModel = procWarnModel(model.getO2(), monitorinstrument, model.getNowTime(), 2, "O2");
        list.add(warningMqModel);
    }

    public void RH(List<WarningMqModel> list, HashOperations<Object, Object, Object> objectObjectObjectHashOperations, Monitorinstrument monitorinstrument, ParamaterModel model, ShowModel showModel, String equipmentno, String data, Date time, Monitorequipmentlastdata monitorequipmentlastdata) {
        showModel.setEquipmentno(equipmentno);
        showModel.setData(model.getRH());
        showModel.setUnit("湿度");
        showModel.setInputdatetime(time);
        objectObjectObjectHashOperations.put("RH", equipmentno, JsonUtil.toJson(showModel));
//        Monitorhumidityrecord monitorhumidityrecord = new Monitorhumidityrecord();
//        monitorhumidityrecord.setEquipmentno(equipmentno);
//        monitorhumidityrecord.setInputdatetime(time);
//        monitorhumidityrecord.setHumidity(model.getRH());
//        monitorhumidityrecord.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
//        try {
//            monitorRhDao.saveAndFlush(monitorhumidityrecord);
//        } catch (Exception e) {
//            log.error("cmdid:" + model.getCmdid() + " SN:" + sn + "湿度插入失败：" + e.getMessage() + "数据：" + JsonUtil.toJson(model));
//        }
        monitorequipmentlastdata.setCurrenthumidity(model.getRH());
        //   log.info("执行插入环境湿度:设备sn号   " + sn + "插入的模型:" + JsonUtil.toJson(monitorhumidityrecord));
        WarningMqModel warningMqModel = procWarnModel(model.getRH(), monitorinstrument, model.getNowTime(), 5, "湿度");
        list.add(warningMqModel);
    }

    public void JQ(List<WarningMqModel> list, HashOperations<Object, Object, Object> objectObjectObjectHashOperations, Monitorinstrument monitorinstrument, ParamaterModel model, ShowModel showModel, String equipmentno, String data, Date time, Monitorequipmentlastdata monitorequipmentlastdata) {
        showModel.setEquipmentno(equipmentno);
        showModel.setData(data);
        showModel.setUnit("甲醛");
        showModel.setInputdatetime(time);
        objectObjectObjectHashOperations.put("JQ", equipmentno, JsonUtil.toJson(showModel));
//        Jqrecord jqrecord = new Jqrecord();
//        jqrecord.setEquipmentno(equipmentno);
//        jqrecord.setInputdatetime(time);
//        jqrecord.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
//        jqrecord.setJq(model.getOX());
//        try {
//            monitorJqDao.saveAndFlush(jqrecord);
//        } catch (Exception e) {
//            log.error("cmdid:" + model.getCmdid() + " SN:" + sn + "JQ插入失败：" + e.getMessage() + "数据：" + JsonUtil.toJson(model));
//        }
        monitorequipmentlastdata.setCurrentformaldehyde(model.getOX());
        // log.info("执行插入环境甲醛:设备sn号   " + sn + "插入的模型:" + JsonUtil.toJson(jqrecord));
        WarningMqModel warningMqModel = procWarnModel(model.getOX(), monitorinstrument, model.getNowTime(), 12, "甲醛");
        list.add(warningMqModel);
    }

    public void PM5(List<WarningMqModel> list, HashOperations<Object, Object, Object> objectObjectObjectHashOperations, Monitorinstrument monitorinstrument, ParamaterModel model, ShowModel showModel, String equipmentno, String data, Date time, Monitorequipmentlastdata monitorequipmentlastdata) {
        showModel.setEquipmentno(equipmentno);
        showModel.setData(data);
        showModel.setUnit("PM5");
        showModel.setInputdatetime(time);
        objectObjectObjectHashOperations.put("PM5", equipmentno, JsonUtil.toJson(showModel));
//        Monitorpm5record monitorpm5record = new Monitorpm5record();
//        monitorpm5record.setEquipmentno(equipmentno);
//        monitorpm5record.setPm5(data);
//        monitorpm5record.setInputdatetime(time);
//        monitorpm5record.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
        monitorequipmentlastdata.setCurrentpm5(data);
//        try {
//            monitorpm5recordDao.saveAndFlush(monitorpm5record);
//        } catch (Exception e) {
//            log.error("cmdid:" + model.getCmdid() + " SN:" + sn + "PM5：" + e.getMessage() + "数据：" + JsonUtil.toJson(model));
//        }
        WarningMqModel warningMqModel = procWarnModel(data, monitorinstrument, model.getNowTime(), 27, "PM5");
        list.add(warningMqModel);
    }

    public void PM05(List<WarningMqModel> list, HashOperations<Object, Object, Object> objectObjectObjectHashOperations, Monitorinstrument monitorinstrument, ParamaterModel model, ShowModel showModel, String equipmentno, String data, Date time, Monitorequipmentlastdata monitorequipmentlastdata) {
        showModel.setEquipmentno(equipmentno);
        showModel.setData(data);
        showModel.setUnit("PM0.5");
        showModel.setInputdatetime(time);
        objectObjectObjectHashOperations.put("PM05", equipmentno, JsonUtil.toJson(showModel));
//        Monitorpm05record monitorpm05record = new Monitorpm05record();
//        monitorpm05record.setEquipmentno(equipmentno);
//        monitorpm05record.setPm05(data);
//        monitorpm05record.setInputdatetime(time);
//        monitorpm05record.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
        monitorequipmentlastdata.setCurrentpm05(data);
//        try {
//            monitorpm05recordDao.saveAndFlush(monitorpm05record);
//        } catch (Exception e) {
//            log.error("cmdid:" + model.getCmdid() + " SN:" + sn + "PM0.5：" + e.getMessage() + "数据：" + JsonUtil.toJson(model));
//        }
        WarningMqModel warningMqModel = procWarnModel(data, monitorinstrument, model.getNowTime(), 28, "PM05");
        list.add(warningMqModel);
    }

    public void Temp1(List<WarningMqModel> list, HashOperations<Object, Object, Object> objectObjectObjectHashOperations, Monitorinstrument monitorinstrument, ParamaterModel model, ShowModel showModel, String equipmentno, String data, Date time, Monitorequipmentlastdata monitorequipmentlastdata) {
        //大屏展示Model //redis获取
        showModel.setEquipmentno(equipmentno);
        showModel.setData(data);
        showModel.setUnit("一路温度");
        showModel.setInputdatetime(time);
        objectObjectObjectHashOperations.put("一路温度", equipmentno, JsonUtil.toJson(showModel));
        monitorequipmentlastdata.setCurrenttemperature1(data);
        WarningMqModel warningMqModel = procWarnModel(data, monitorinstrument, model.getNowTime(), 13, "一路温度");
        list.add(warningMqModel);
    }

    public void Temp2(List<WarningMqModel> list, HashOperations<Object, Object, Object> objectObjectObjectHashOperations, Monitorinstrument monitorinstrument, ParamaterModel model, ShowModel showModel, String equipmentno, String data, Date time, Monitorequipmentlastdata monitorequipmentlastdata) {
        //大屏展示Model //redis获取
        showModel.setEquipmentno(equipmentno);
        showModel.setData(data);
        showModel.setUnit("二路温度");
        showModel.setInputdatetime(time);
        objectObjectObjectHashOperations.put("二路温度", equipmentno, JsonUtil.toJson(showModel));
        monitorequipmentlastdata.setCurrenttemperature2(data);
        WarningMqModel warningMqModel = procWarnModel(data, monitorinstrument, model.getNowTime(), 14, "二路温度");
        list.add(warningMqModel);
    }

    public void Temp3(List<WarningMqModel> list, HashOperations<Object, Object, Object> objectObjectObjectHashOperations, Monitorinstrument monitorinstrument, ParamaterModel model, ShowModel showModel, String equipmentno, String data, Date time, Monitorequipmentlastdata monitorequipmentlastdata) {
        //大屏展示Model //redis获取
        showModel.setEquipmentno(equipmentno);
        showModel.setData(data);
        showModel.setUnit("三路温度");
        showModel.setInputdatetime(time);
        objectObjectObjectHashOperations.put("三路温度", equipmentno, JsonUtil.toJson(showModel));
        monitorequipmentlastdata.setCurrenttemperature3(data);
        WarningMqModel warningMqModel = procWarnModel(data, monitorinstrument, model.getNowTime(), 15, "三路温度");
        list.add(warningMqModel);
    }

    public void Temp4(List<WarningMqModel> list, HashOperations<Object, Object, Object> objectObjectObjectHashOperations, Monitorinstrument monitorinstrument, ParamaterModel model, ShowModel showModel, String equipmentno, String data, Date time, Monitorequipmentlastdata monitorequipmentlastdata) {
        //大屏展示Model //redis获取
        showModel.setEquipmentno(equipmentno);
        showModel.setData(data);
        showModel.setUnit("四路温度");
        showModel.setInputdatetime(time);
        objectObjectObjectHashOperations.put("四路温度", equipmentno, JsonUtil.toJson(showModel));
        monitorequipmentlastdata.setCurrenttemperature4(data);
        WarningMqModel warningMqModel = procWarnModel(data, monitorinstrument, model.getNowTime(), 16, "四路温度");
        list.add(warningMqModel);
    }

    public void Temp5(List<WarningMqModel> list, HashOperations<Object, Object, Object> objectObjectObjectHashOperations, Monitorinstrument monitorinstrument, ParamaterModel model, ShowModel showModel, String equipmentno, String data, Date time, Monitorequipmentlastdata monitorequipmentlastdata) {
        //大屏展示Model //redis获取
        showModel.setEquipmentno(equipmentno);
        showModel.setData(data);
        showModel.setUnit("五路温度");
        showModel.setInputdatetime(time);
        objectObjectObjectHashOperations.put("五路温度", equipmentno, JsonUtil.toJson(showModel));
        monitorequipmentlastdata.setCurrenttemperature5(data);
        WarningMqModel warningMqModel = procWarnModel(data, monitorinstrument, model.getNowTime(), 17, "五路温度");
        list.add(warningMqModel);
    }

    public void Temp6(List<WarningMqModel> list, HashOperations<Object, Object, Object> objectObjectObjectHashOperations, Monitorinstrument monitorinstrument, ParamaterModel model, ShowModel showModel, String equipmentno, String data, Date time, Monitorequipmentlastdata monitorequipmentlastdata) {
        //大屏展示Model //redis获取
        showModel.setEquipmentno(equipmentno);
        showModel.setData(data);
        showModel.setUnit("六路温度");
        showModel.setInputdatetime(time);
        objectObjectObjectHashOperations.put("六路温度", equipmentno, JsonUtil.toJson(showModel));
        monitorequipmentlastdata.setCurrenttemperature6(data);
        WarningMqModel warningMqModel = procWarnModel(data, monitorinstrument, model.getNowTime(), 18, "六路温度");
        list.add(warningMqModel);
    }

    public void Temp7(List<WarningMqModel> list, HashOperations<Object, Object, Object> objectObjectObjectHashOperations, Monitorinstrument monitorinstrument, ParamaterModel model, ShowModel showModel, String equipmentno, String data, Date time, Monitorequipmentlastdata monitorequipmentlastdata) {
        //大屏展示Model //redis获取
        showModel.setEquipmentno(equipmentno);
        showModel.setData(data);
        showModel.setUnit("七路温度");
        showModel.setInputdatetime(time);
        objectObjectObjectHashOperations.put("七路温度", equipmentno, JsonUtil.toJson(showModel));
        monitorequipmentlastdata.setCurrenttemperature7(data);
        WarningMqModel warningMqModel = procWarnModel(data, monitorinstrument, model.getNowTime(), 19, "七路温度");
        list.add(warningMqModel);
    }

    public void Temp8(List<WarningMqModel> list, HashOperations<Object, Object, Object> objectObjectObjectHashOperations, Monitorinstrument monitorinstrument, ParamaterModel model, ShowModel showModel, String equipmentno, String data, Date time, Monitorequipmentlastdata monitorequipmentlastdata) {
        //大屏展示Model //redis获取
        showModel.setEquipmentno(equipmentno);
        showModel.setData(data);
        showModel.setUnit("八路温度");
        showModel.setInputdatetime(time);
        objectObjectObjectHashOperations.put("八路温度", equipmentno, JsonUtil.toJson(showModel));
        monitorequipmentlastdata.setCurrenttemperature8(data);
        WarningMqModel warningMqModel = procWarnModel(data, monitorinstrument, model.getNowTime(), 20, "八路温度");
        list.add(warningMqModel);
    }

    public void Temp9(List<WarningMqModel> list, HashOperations<Object, Object, Object> objectObjectObjectHashOperations, Monitorinstrument monitorinstrument, ParamaterModel model, ShowModel showModel, String equipmentno, String data, Date time, Monitorequipmentlastdata monitorequipmentlastdata) {
        //大屏展示Model //redis获取
        showModel.setEquipmentno(equipmentno);
        showModel.setData(data);
        showModel.setUnit("九路温度");
        showModel.setInputdatetime(time);
        objectObjectObjectHashOperations.put("九路温度", equipmentno, JsonUtil.toJson(showModel));
        monitorequipmentlastdata.setCurrenttemperature9(data);
        WarningMqModel warningMqModel = procWarnModel(data, monitorinstrument, model.getNowTime(), 21, "九路温度");
        list.add(warningMqModel);
    }

    public void Temp10(List<WarningMqModel> list, HashOperations<Object, Object, Object> objectObjectObjectHashOperations, Monitorinstrument monitorinstrument, ParamaterModel model, ShowModel showModel, String equipmentno, String data, Date time, Monitorequipmentlastdata monitorequipmentlastdata) {
        //大屏展示Model //redis获取
        showModel.setEquipmentno(equipmentno);
        showModel.setData(data);
        showModel.setUnit("十路温度");
        showModel.setInputdatetime(time);
        objectObjectObjectHashOperations.put("十路温度", equipmentno, JsonUtil.toJson(showModel));
        monitorequipmentlastdata.setCurrenttemperature10(data);
        WarningMqModel warningMqModel = procWarnModel(data, monitorinstrument, model.getNowTime(), 22, "十路温度");
        list.add(warningMqModel);
    }

    /**
     * 值校验
     *
     * @param o
     * @param data
     * @return
     */
    public String calibration(String o, String data) {

        InstrumentInfoModel instrumentInfoModel = JsonUtil.toBean(o, InstrumentInfoModel.class);
        List<String> list = new ArrayList<String>();
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");
        list.add("F");
        if (list.contains(data)) {
            return data;
        }
        String calibration = instrumentInfoModel.getCalibration();
        if (StringUtils.isEmpty(calibration)) {
            return data;
        }
        //去除空
        String s = calibration.replaceAll(" ", "");
        try {

            return new BigDecimal(data).add(new BigDecimal(s)).toString();
        } catch (Exception e) {
            return data;
        }


    }


    public void dataTimeOut(String equipmentno) {

        HashOperations<Object, Object, Object> redisTemple = redisTemplateUtil.opsForHash();

        redisTemple.put("timeOut", "equipmentno:" + equipmentno, TimeHelper.getCurrentTimes());
//        if (redisTemple.hasKey("disable", "equipmentno:" + equipmentno)) {
//            //表示当前设备之前禁用，现在数据重新上传，又启用了
//            redisTemple.delete("disable", "equipmentno:" + equipmentno);
//            //启用设备
//            monitorequipmentDao.updateMonitorequipmentAble(equipmentno);
//            // 报警通知
//            //查询当前设备
//            TimeoutEquipment one = monitorInstrumentMapper.getOne(equipmentno);
//            one.setDisabletype("4");//解除报警
//            messagePushService.pushMessage5(JsonUtil.toJson(one));
//        }
    }

}
