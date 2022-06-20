package com.hc.clickhouse.po;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@TableName(value = "monitorequipmentlastdata")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Monitorequipmentlastdata implements Serializable {

    private long id;

    private String sn;

    private String cmdid;
    /**
     * 设备编号
     */
    private String equipmentno;

    /**
     * 记录时间
     */
    @Excel(name = "记录时间",orderNum = "1",format = "yyyy-MM-ss HH:mm")
    private Date inputdatetime;

    /**
     * 医院编号
     */
    private String hospitalcode;

    /**
     * 当前温度
     */
    @Excel(name = "当前温度",orderNum = "3")
    private String currenttemperature;

    /**
     * 当前二氧化碳
     */
    @Excel(name = "当前二氧化碳",orderNum = "4")
    private String currentcarbondioxide;

    /**
     * 当前O2
     */
    @Excel(name = "当前O2",orderNum = "5")
    private String currento2;

    /**
     * 当前气流
     */
    @Excel(name = "当前气流",orderNum = "6")
    private String currentairflow;

    /**
     * 当前开门记录
     */
    @Excel(name = "当前开门记录",orderNum = "7")
    private String currentdoorstate;

    /**
     * 当前湿度
     */
    @Excel(name = "当前湿度",orderNum = "8")
    private String currenthumidity;

    /**
     * 当前空气质量
     */
    @Excel(name = "当前空气质量",orderNum = "9")
    private String currentvoc;

    /**
     * 当前甲醛
     */
    @Excel(name = "当前甲醛",orderNum = "10")
    private String currentformaldehyde;

    /**
     * 当前PM2_5
     */
    @Excel(name = "当前PM2_5",orderNum = "11")
    private String currentpm25;
    /**
     * 培养箱气流
     */
    @Excel(name = "培养箱气流",orderNum = "12")
    private String currentairflow1;

    /**
     * 当前PM10
     */
    @Excel(name = "当前PM10",orderNum = "13")
    private String currentpm10;
    /**
     * 当前PM0.5
     */
    @Excel(name = "当前PM0.5",orderNum = "14")
    private String currentpm05;
    /**
     * 当前PM5
     */
    @Excel(name = "当前PM5",orderNum = "15")
    private String currentpm5;

    /**
     * 当前市电是否异常
     */
    @Excel(name = "当前市电",orderNum = "16")
    private String currentups;

    /**
     * 当前电量
     */
    @Excel(name = "当前电量",orderNum = "17")
    private String currentqc;
    /**
     * 当前锁电量
     */
    @Excel(name = "当前锁电量",orderNum = "18")
    private String currentqcl;

    /**
     * 当前温度
     */
    @Excel(name = "当前温度1",orderNum = "19")
    private String currenttemperature1;
    @Excel(name = "当前温度2",orderNum = "20")
    private String currenttemperature2;
    @Excel(name = "当前温度3",orderNum = "21")
    private String currenttemperature3;
    @Excel(name = "当前温度4",orderNum = "22")
    private String currenttemperature4;
    @Excel(name = "当前温度5",orderNum = "23")
    private String currenttemperature5;
    @Excel(name = "当前温度6",orderNum = "24")
    private String currenttemperature6;
    @Excel(name = "当前温度7",orderNum = "25")
    private String currenttemperature7;
    @Excel(name = "当前温度8",orderNum = "26")
    private String currenttemperature8;
    @Excel(name = "当前温度9",orderNum = "27")
    private String currenttemperature9;
    @Excel(name = "当前温度10",orderNum = "28")
    private String currenttemperature10;

    /**
     * 左路温度  左舱室温度
     */
    @Excel(name = "左路温度  左舱室温度",orderNum = "29")
    private String currentlefttemperature;
    /**
     * 右路温度  右舱室温度
     */
    @Excel(name = "右路温度  右舱室温度",orderNum = "30")
    private String currentrigthtemperature;
    /**
     * 温差
     */
    @Excel(name = "温差",orderNum = "31")
    private String currenttemperaturediff;
    /**
     * 左盖板温度
     */
    @Excel(name = "左盖板温度",orderNum = "32")
    private String currentleftcovertemperature;
    /**
     * 左底板温度
     */
    @Excel(name = "左底板温度",orderNum = "33")
    private String currentleftendtemperature;
    /**
     * 左气流
     */
    @Excel(name = "左气流",orderNum = "34")
    private String currentleftairflow;
    /**
     * 右盖板温度
     */
    @Excel(name = "右盖板温度",orderNum = "35")
    private String currentrightcovertemperature;
    /**
     * 右底板温度
     */
    @Excel(name = "右底板温度",orderNum = "36")
    private String currentrightendtemperature;
    /**
     * 右气流
     */
    @Excel(name = "右气流",orderNum = "37")
    private String currentrightairflow;
    /**
     * 氮气
     */
    @Excel(name = "氮气",orderNum = "38")
    private String currentn2;
    /**
     * 左舱室湿度
     */
    @Excel(name = "左舱室湿度",orderNum = "39")
    @TableField(value = "leftCompartmentHumidity")
    private String leftCompartmentHumidity;
    /**
     * 右舱室湿度
     */
    @Excel(name = "右舱室湿度",orderNum = "40")
    @TableField(value = "rightCompartmentHumidity")
    private String rightCompartmentHumidity;
    /**
     * 电压
     */
    @Excel(name = "电压",orderNum = "41")
    private String voltage;
    /**
     * 电流
     */
    @Excel(name = "电流",orderNum = "42")
    private String current;
    /**
     * 功率
     */
    @Excel(name = "功率",orderNum = "43")
    private String power;


    /**
     * mt310模式
     */
    @Excel(name = "mt310模式",orderNum = "44")
    private String model;

    /**
     * mt310探头一模式
     */
    @Excel(name = "mt310探头一模式",orderNum = "45",replace = {"温度_1","湿度_2","O2浓度_3","CO2 浓度_4"})
    private String probe1model;

    /**
     * mt310探头二模式
     */
    @Excel(name = "mt310探头二模式",orderNum = "47",replace = {"温度_1","湿度_2","O2浓度_3","CO2 浓度_4"})
    private String probe2model;

    /**
     * mt310探头三模式
     */
    @Excel(name = "mt310探头三模式",orderNum = "49",replace = {"温度_1","湿度_2","O2浓度_3","CO2 浓度_4"})
    private String probe3model;


    /**
     * mt310探头一数据
     */
    @Excel(name = "mt310探头一数据",orderNum = "46")
    private String probe1data;

    /**
     * mt310探头二数据
     */
    @Excel(name = "mt310探头二数据",orderNum = "48")
    private String probe2data;

    /**
     * mt310探头三数据
     */
    @Excel(name = "mt310探头三数据",orderNum = "50")
    private String probe3data;

}
