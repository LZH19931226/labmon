package com.hc.application.response;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

@Data
public class LastDataResult {
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
    private Date inputdatetime;

    /**
     * 医院编号
     */
    private String hospitalcode;

    /**
     * 当前温度
     */
    private String currenttemperature;

    /**
     * 当前二氧化碳
     */
    private String currentcarbondioxide ;

    /**
     * 当前O2
     */
    private String currento2;

    /**
     * 当前气流
     */
    private String currentairflow ;

    /**
     * 当前开门记录
     */
    private String currentdoorstate;

    /**
     * 当前开门记录2
     */
    private String currentdoorstate2;

    /**
     * 当前湿度
     */
    private String currenthumidity ;

    /**
     * 当前空气质量
     */
    private String currentvoc;

    /**
     * 当前甲醛
     */
    private String currentformaldehyde ;

    /**
     * 当前PM2_5
     */
    private String currentpm25 ;
    /**
     * 培养箱气流
     */
    private String currentairflow1 ;

    /**
     * 当前PM10
     */
    private String currentpm10 ;
    /**
     * 当前PM0.5
     */
    private String currentpm05 ;
    /**
     * 当前PM5
     */
    private String currentpm5 ;

    /**
     * 当前市电是否异常
     */
    private String currentups   ;

    /**
     * 当前电量
     */
    private String currentqc   ;
    /**
     * 当前锁电量
     */
    private String currentqcl   ;

    /**
     * 当前温度
     */
    private String currenttemperature1   ;
    private String currenttemperature2   ;

    private String currenttemperature3   ;
    private String currenttemperature4  ;
    private String currenttemperature5  ;
    private String currenttemperature6  ;
    private String currenttemperature7  ;
    private String currenttemperature8  ;
    private String currenttemperature9  ;
    private String currenttemperature10  ;

    /**
     * 左路温度  左舱室温度
     */
    private String currentlefttemperature  ;
    /**
     * 右路温度  右舱室温度
     */
    private String currentrigthtemperature  ;
    /**
     * 温差
     */
    private String currenttemperaturediff  ;
    /**
     * 左盖板温度
     */
    private String currentleftcovertemperature  ;
    /**
     * 左底板温度
     */
    private String currentleftendtemperature  ;
    /**
     * 左气流
     */
    private String currentleftairflow  ;
    /**
     * 右盖板温度
     */
    private String currentrightcovertemperature  ;
    /**
     * 右底板温度
     */
    private String currentrightendtemperature  ;
    /**
     * 右气流
     */
    private String currentrightairflow  ;
    /**
     * 氮气
     */
    private String currentn2  ;
    /**
     * 左舱室湿度
     */
    @TableField(value = "leftCompartmentHumidity")
    private String leftCompartmentHumidity  ;
    /**
     * 右舱室湿度
     */
    @TableField(value = "rightCompartmentHumidity")
    private String rightCompartmentHumidity  ;
    /**
     * 电压
     */
    private String voltage  ;
    /**
     * 电流
     */
    private String qccurrent  ;
    /**
     * 功率
     */
    private String power  ;
    /**
     * mt310模式
     */
    private String model  ;

    /**
     * mt310探头一模式
     */
    private String probe1model  ;

    /**
     * mt310探头二模式
     */
    private String probe2model  ;

    /**
     * mt310探头三模式
     */
    private String probe3model  ;


    /**
     * mt310探头一数据
     */
    private String probe1data  ;

    /**
     * mt310探头二数据
     */
    private String probe2data  ;

    /**
     * mt310探头三数据
     */
    private String probe3data  ;

    /** 液位 */
    @TableField(value = "liquidLevel")
    private String liquidLevel;

    /** 预留字段1 */
    private String remark1;

    /** 预留字段2 */
    private String remark2;

    /** 预留字段3 */
    private String remark3;

    /** 预留字段4 */
    private String remark4;

    /** 预留字段5 */
    private String remark5;

    /** 预留字段6 */
    private String remark6;

    /** 预留字段7 */
    private String remark7;

    /** 预留字段8 */
    private String remark8;

    /** 预留字段9 */
    private String remark9;

    /** 预留字段10 */
    private String remark10;

    /** 单位*/
    private String unit;

    /** 外置探头co2 */
    private String outerCO2;

    /** 外置探头o2 */
    private String outerO2;
}
