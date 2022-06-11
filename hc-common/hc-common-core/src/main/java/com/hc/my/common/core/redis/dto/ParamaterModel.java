package com.hc.my.common.core.redis.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 16956 on 2018-07-31.
 */

@Data
@EqualsAndHashCode
public class ParamaterModel implements Serializable {
    //设备sn号"
    private String SN;
    //命令id"
    private String cmdid;
    //"CO2"
    private String CO2;
    //"O2"
    private String O2;
    //"空气质量"
    private String VOC;
    //一路温度"
    private String TEMP;
    //二路温度"
    private String TEMP2;
    //"三路温度"
    private String TEMP3;
    //"四路温度"
    private String TEMP4;
    //五路温度"
    private String TEMP5;
    //六路温度"
    private String TEMP6;
    //"七路温度"
    private String TEMP7;
    //"八路温度"
    private String TEMP8;
    //"九路温度"
    private String TEMP9;
    //"十路温度"
    private String TEMP10;
    //"气流"
    private String airflow;
    //"湿度"
    private String RH;
    //"压力"
    private String PRESS;
    //"电量"
    private String QC;
    //"PM2.5"
    private String PM25;
    //"PM10"
    private String PM10;
    //"PM0.5"
    private String PM05;
    //"PM5.0"
    private String PM50;
    //"N2压力"
    private String N2;
    //1：市电通->市电断 2：市电断->市电断 3：市电断->市电通  4：市电正常 5：市电异常
    private String UPS;
    //开关量编号（目前只支持两路开关量 1 和 2）
    private String DOOR;
    //开关量编号报警类型/状态 1：开->关 2：关->开 3：持续关 4：持续开
    private String DOORZ;
    //"甲醛")
    private String OX;
    //左舱室温度
    private String leftCompartmentTemp;
    //左舱室流量
    private String leftCompartmentFlow;
    //左舱室湿度
    private String leftCompartmentHumidity;
    //右舱室温度
    private String rightCompartmentTemp;
    //右舱室流量
    private String rightCompartmentFlow;
    //右舱室湿度
    private String rightCompartmentHumidity;
    //电压
    private String voltage;
    //电流
    private String current;
    //功率
    private String power;
    //当前时间
    private Date nowTime;
    //"通道id"
    private String channelId;

    private String data;


    /**
     * mt310模式
     */
    private String model;

    /**
     * 探头1温度
     */
    private String probeTemperature1;

    /**
     * 探头1二氧化碳
     */
    private String probeCarbondioxide1;

    /**
     * 探头1 O2
     */
    private String probeOxygen1;

    /**
     * 探头1湿度
     */
    private String probehumidity1;

    /**
     * 探头2温度
     */
    private String probeTemperature2;

    /**
     * 探头2二氧化碳
     */
    private String probeCarbondioxide2;

    /**
     * 探头2 O2
     */
    private String probeOxygen2;

    /**
     * 探头2湿度
     */
    private String probehumidity2;

    /**
     * 探头3温度
     */
    private String probeTemperature3;

    /**
     * 探头3二氧化碳
     */
    private String probeCarbondioxide3;

    /**
     * 探头3 O2
     */
    private String probeOxygen3;

    /**
     * 探头3湿度
     */
    private String probehumidity3;
}
