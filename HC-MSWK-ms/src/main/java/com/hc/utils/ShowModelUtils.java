package com.hc.utils;

import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.my.common.core.redis.dto.InstrumentInfoDto;
import com.hc.my.common.core.redis.dto.ProbeInfoDto;
import com.hc.my.common.core.util.RegularUtil;
import com.hc.po.Monitorinstrument;
import com.hc.model.WarningMqModel;
import com.hc.my.common.core.redis.dto.ParamaterModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author LiuZhiHao
 * @date 2020/8/7 10:40
 * 描述:
 **/
@Component
public class ShowModelUtils {



    public WarningMqModel procWarnModel(String data, Monitorinstrument monitorinstrument, Date date, Integer instrumentconfigid, String unit) {
        WarningMqModel warningMqModel = new WarningMqModel();
        warningMqModel.setCurrrentData(data);
        warningMqModel.setDate(date);
        warningMqModel.setInstrumentconfigid(instrumentconfigid);
        warningMqModel.setMonitorinstrument(monitorinstrument);
        warningMqModel.setUnit(unit);
        warningMqModel.setSn(monitorinstrument.getSn());
        return warningMqModel;
    }

    public void Temp(List<WarningMqModel> list, Monitorinstrument monitorinstrument, ParamaterModel model, String data,Monitorequipmentlastdata monitorequipmentlastdata) {
        monitorequipmentlastdata.setCurrenttemperature(data);
        //执行报警服务
        WarningMqModel warningMqModel97 = procWarnModel(model.getTEMP(), monitorinstrument, model.getNowTime(), 4, "温度");
        list.add(warningMqModel97);
    }

    public void CO2(List<WarningMqModel> list, Monitorinstrument monitorinstrument, ParamaterModel model,  String equipmentno, String data, Date time, Monitorequipmentlastdata monitorequipmentlastdata) {
        monitorequipmentlastdata.setCurrentcarbondioxide(model.getCO2());
        WarningMqModel warningMqModel = procWarnModel(model.getCO2(), monitorinstrument, model.getNowTime(), 1, "CO2");
        list.add(warningMqModel);
    }

    public void O2(List<WarningMqModel> list, Monitorinstrument monitorinstrument, ParamaterModel model, String equipmentno, String data, Date time, Monitorequipmentlastdata monitorequipmentlastdata) {
        monitorequipmentlastdata.setCurrento2(model.getO2());
        WarningMqModel warningMqModel = procWarnModel(model.getO2(), monitorinstrument, model.getNowTime(), 2, "O2");
        list.add(warningMqModel);
    }

    public void RH(List<WarningMqModel> list,  Monitorinstrument monitorinstrument, ParamaterModel model,  String equipmentno, String data, Date time, Monitorequipmentlastdata monitorequipmentlastdata) {
        monitorequipmentlastdata.setCurrenthumidity(model.getRH());
        WarningMqModel warningMqModel = procWarnModel(model.getRH(), monitorinstrument, model.getNowTime(), 5, "湿度");
        list.add(warningMqModel);
    }

    public void JQ(List<WarningMqModel> list, Monitorinstrument monitorinstrument, ParamaterModel model,  String equipmentno, String data, Date time, Monitorequipmentlastdata monitorequipmentlastdata) {
        monitorequipmentlastdata.setCurrentformaldehyde(model.getOX());
        WarningMqModel warningMqModel = procWarnModel(model.getOX(), monitorinstrument, model.getNowTime(), 12, "甲醛");
        list.add(warningMqModel);
    }

    public void PM5(List<WarningMqModel> list,Monitorinstrument monitorinstrument, ParamaterModel model, String equipmentno, String data, Date time, Monitorequipmentlastdata monitorequipmentlastdata) {
        monitorequipmentlastdata.setCurrentpm5(data);
        WarningMqModel warningMqModel = procWarnModel(data, monitorinstrument, model.getNowTime(), 27, "PM5");
        list.add(warningMqModel);
    }

    public void PM05(List<WarningMqModel> list, Monitorinstrument monitorinstrument, ParamaterModel model, String equipmentno, String data, Date time, Monitorequipmentlastdata monitorequipmentlastdata) {
        monitorequipmentlastdata.setCurrentpm05(data);
        WarningMqModel warningMqModel = procWarnModel(data, monitorinstrument, model.getNowTime(), 28, "PM05");
        list.add(warningMqModel);
    }

    public void Temp1(List<WarningMqModel> list,  Monitorinstrument monitorinstrument, ParamaterModel model,  String equipmentno, String data, Date time, Monitorequipmentlastdata monitorequipmentlastdata) {
        monitorequipmentlastdata.setCurrenttemperature1(data);
        WarningMqModel warningMqModel = procWarnModel(data, monitorinstrument, model.getNowTime(), 13, "一路温度");
        list.add(warningMqModel);
    }

    public void Temp2(List<WarningMqModel> list,  Monitorinstrument monitorinstrument, ParamaterModel model,  String equipmentno, String data, Date time, Monitorequipmentlastdata monitorequipmentlastdata) {
        monitorequipmentlastdata.setCurrenttemperature2(data);
        WarningMqModel warningMqModel = procWarnModel(data, monitorinstrument, model.getNowTime(), 14, "二路温度");
        list.add(warningMqModel);
    }

    public void Temp3(List<WarningMqModel> list, Monitorinstrument monitorinstrument, ParamaterModel model,  String equipmentno, String data, Date time, Monitorequipmentlastdata monitorequipmentlastdata) {
        monitorequipmentlastdata.setCurrenttemperature3(data);
        WarningMqModel warningMqModel = procWarnModel(data, monitorinstrument, model.getNowTime(), 15, "三路温度");
        list.add(warningMqModel);
    }

    public void Temp4(List<WarningMqModel> list, Monitorinstrument monitorinstrument, ParamaterModel model,  String equipmentno, String data, Date time, Monitorequipmentlastdata monitorequipmentlastdata) {
        monitorequipmentlastdata.setCurrenttemperature4(data);
        WarningMqModel warningMqModel = procWarnModel(data, monitorinstrument, model.getNowTime(), 16, "四路温度");
        list.add(warningMqModel);
    }

    public void Temp5(List<WarningMqModel> list, Monitorinstrument monitorinstrument, ParamaterModel model,  String equipmentno, String data, Date time, Monitorequipmentlastdata monitorequipmentlastdata) {
        monitorequipmentlastdata.setCurrenttemperature5(data);
        WarningMqModel warningMqModel = procWarnModel(data, monitorinstrument, model.getNowTime(), 17, "五路温度");
        list.add(warningMqModel);
    }

    public void Temp6(List<WarningMqModel> list,  Monitorinstrument monitorinstrument, ParamaterModel model,  String equipmentno, String data, Date time, Monitorequipmentlastdata monitorequipmentlastdata) {
        monitorequipmentlastdata.setCurrenttemperature6(data);
        WarningMqModel warningMqModel = procWarnModel(data, monitorinstrument, model.getNowTime(), 18, "六路温度");
        list.add(warningMqModel);
    }

    public void Temp7(List<WarningMqModel> list,  Monitorinstrument monitorinstrument, ParamaterModel model,  String equipmentno, String data, Date time, Monitorequipmentlastdata monitorequipmentlastdata) {
        monitorequipmentlastdata.setCurrenttemperature7(data);
        WarningMqModel warningMqModel = procWarnModel(data, monitorinstrument, model.getNowTime(), 19, "七路温度");
        list.add(warningMqModel);
    }

    public void Temp8(List<WarningMqModel> list, Monitorinstrument monitorinstrument, ParamaterModel model,  String equipmentno, String data, Date time, Monitorequipmentlastdata monitorequipmentlastdata) {
        monitorequipmentlastdata.setCurrenttemperature8(data);
        WarningMqModel warningMqModel = procWarnModel(data, monitorinstrument, model.getNowTime(), 20, "八路温度");
        list.add(warningMqModel);
    }

    public void Temp9(List<WarningMqModel> list, Monitorinstrument monitorinstrument, ParamaterModel model,  String equipmentno, String data, Date time, Monitorequipmentlastdata monitorequipmentlastdata) {
        monitorequipmentlastdata.setCurrenttemperature9(data);
        WarningMqModel warningMqModel = procWarnModel(data, monitorinstrument, model.getNowTime(), 21, "九路温度");
        list.add(warningMqModel);
    }

    public void Temp10(List<WarningMqModel> list, Monitorinstrument monitorinstrument, ParamaterModel model, String equipmentno, String data, Date time, Monitorequipmentlastdata monitorequipmentlastdata) {
        monitorequipmentlastdata.setCurrenttemperature10(data);
        WarningMqModel warningMqModel = procWarnModel(data, monitorinstrument, model.getNowTime(), 22, "十路温度");
        list.add(warningMqModel);
    }

    /**
     * 值校准
     *
     * @param data
     * @return
     */
    public String calibration(InstrumentInfoDto instrumentInfoDto, String data) {
        //如果是异常值则直接返回异常值
        if (!RegularUtil.checkContainsNumbers(data)) {
            return data;
        }
        String calibration = instrumentInfoDto.getCalibration();
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


}
