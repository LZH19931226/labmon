package com.hc.model.SingleTimeExcle;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by xxf on 2019-02-14.
 */
@Getter
@Setter
@ToString
@ApiModel("当前医院所有设备当前时间点当前值")
@Component
public class SingleTimeEquipmentModel {

    private String hospitalcode;


    @Excel(name = "设备名称",orderNum = "0")
    private String equipmentname;

    private String equipmenttypeid;
    @Excel(name = "时间",orderNum = "1")
    private String inputdatetime;
    @Excel(name = "温度",orderNum = "2")
    private String currenttemperature;
    @Excel(name = "CO2",orderNum = "3")
    private String currentcarbondioxide;

    @Excel(name = "O2",orderNum = "4")
    private String currento2;
    @Excel(name = "压力",orderNum = "5")
    private String currentairflow;
    @Excel(name = "湿度",orderNum = "6")
    private String currenthumidity;
    @Excel(name = "空气质量",orderNum = "7")
    private String currentvoc;
    @Excel(name = "甲醛",orderNum = "8")
    private String currentformaldehyde;
    @Excel(name = "PM2.5",orderNum = "9")
    private String currentpm25;
    @Excel(name = "PM10",orderNum = "10")
    private String currentpm10;
    @Excel(name = "PM5",orderNum = "11")
    private String currentpm5;
    @Excel(name = "PM0.5",orderNum = "12")
    private String currentpm05;
    @Excel(name = "培养箱压力",orderNum = "13")
    private String currentairflow1;
    @Excel(name = "培养箱左舱室温度",orderNum = "14")
    private String currentlefttemperature;
    @Excel(name = "培养箱右舱室温度",orderNum = "15")
    private String currentrigthtemperature;
    @Excel(name = "一路温度",orderNum = "16")
    private String currenttemperature1;
    @Excel(name = "二路温度",orderNum = "17")
    private String currenttemperature2;
    @Excel(name = "三路温度",orderNum = "18")
    private String currenttemperature3;
    @Excel(name = "四路温度",orderNum = "19")
    private String currenttemperature4;
    @Excel(name = "五路温度",orderNum = "20")
    private String currenttemperature5;
    @Excel(name = "六路温度",orderNum = "21")
    private String currenttemperature6;
    @Excel(name = "七路温度",orderNum = "22")
    private String currenttemperature7;
    @Excel(name = "八路温度",orderNum = "23")
    private String currenttemperature8;
    @Excel(name = "九路温度",orderNum = "24")
    private String currenttemperature9;
    @Excel(name = "十路温度",orderNum = "25")
    private String currenttemperature10;
    @Excel(name = "左盖板温度",orderNum = "26")
    private String currentleftcovertemperature;
    @Excel(name = "左底板温度",orderNum = "27")
    private String currentleftendtemperature;
    @Excel(name = "左气流",orderNum = "28")
    private String currentleftairflow;
    @Excel(name = "右盖板温度",orderNum = "29")
    private String currentrightcovertemperature;
    @Excel(name = "右底板温度",orderNum = "30")
    private String currentrightendtemperature;
    @Excel(name = "右气流",orderNum = "31")
    private String currentrightairflow;


}
