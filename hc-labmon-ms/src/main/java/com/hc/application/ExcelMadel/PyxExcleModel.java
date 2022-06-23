package com.hc.application.ExcelMadel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * excel模型 培养箱
 */
@Data
public class PyxExcleModel {
    @Excel(name = "设备名称", orderNum = "0")
    private String equipmentname;
    @Excel(name = "记录时间",  orderNum = "1",format = "yyyy-MM-dd HH:mm")
    private Date inputdatetime;

    /**
     * 当前温度
     */
    @Excel(name = "温度", orderNum = "2")
    private String currenttemperature;

    /**
     * 当前二氧化碳
     */
    @Excel(name = "CO2", orderNum = "3")
    private String currentcarbondioxide;

    /**
     * 当前O2
     */
    @Excel(name = "O2", orderNum = "4")
    private String currento2;
    @Excel(name = "湿度", orderNum = "5")
    private String currenthumidity;
    @Excel(name = "气体流量", orderNum = "6")
    private String currentairflow1;
    @Excel(name = "左舱室温度", orderNum = "7")
    private String currentlefttemperature;
    @Excel(name = "右舱室温度", orderNum = "8")
    private String currentrigthtemperature;
    @Excel(name = "一路温度", orderNum = "9")
    private String currenttemperature1;
    @Excel(name = "二路温度", orderNum = "10")
    private String currenttemperature2;
    @Excel(name = "三路温度", orderNum = "11")
    private String currenttemperature3;
    @Excel(name = "四路温度", orderNum = "12")
    private String currenttemperature4;
    @Excel(name = "五路温度", orderNum = "13")
    private String currenttemperature5;
    @Excel(name = "六路温度",orderNum = "14")
    private String currenttemperature6;
    @Excel(name = "七路温度",orderNum = "15")
    private String currenttemperature7;
    @Excel(name = "八路温度",orderNum = "16")
    private String currenttemperature8;
    @Excel(name = "九路温度",orderNum = "17")
    private String currenttemperature9;
    @Excel(name = "十路温度",orderNum = "18")
    private String currenttemperature10;
    @Excel(name = "左盖板温度",orderNum = "19")
    private String currentleftcovertemperature;
    @Excel(name = "左底板温度",orderNum = "20")
    private String currentleftendtemperature;
    @Excel(name = "左气流",orderNum = "21")
    private String currentleftairflow;
    @Excel(name = "右盖板温度",orderNum = "22")
    private String currentrightcovertemperature;
    @Excel(name = "右底板温度",orderNum = "23")
    private String currentrightendtemperature;
    @Excel(name = "右气流",orderNum = "24")
    private String currentrightairflow;
    /**
     * mt310模式
     */
    @Excel(name = "mt310模式",orderNum = "25")
    private String model;

    /**
     * mt310探头一模式
     */
    @Excel(name = "mt310探头一模式",orderNum = "26",replace = {"温度_1","湿度_2","O2浓度_3","CO2 浓度_4"})
    private String probe1model;

    /**
     * mt310探头二模式
     */
    @Excel(name = "mt310探头二模式",orderNum = "28",replace = {"温度_1","湿度_2","O2浓度_3","CO2 浓度_4"})
    private String probe2model;

    /**
     * mt310探头三模式
     */
    @Excel(name = "mt310探头三模式",orderNum = "30",replace = {"温度_1","湿度_2","O2浓度_3","CO2 浓度_4"})
    private String probe3model;


    /**
     * mt310探头一数据
     */
    @Excel(name = "mt310探头一数据",orderNum = "27")
    private String probe1data;

    /**
     * mt310探头二数据
     */
    @Excel(name = "mt310探头二数据",orderNum = "29")
    private String probe2data;

    /**
     * mt310探头三数据
     */
    @Excel(name = "mt310探头三数据",orderNum = "31")
    private String probe3data;
}
