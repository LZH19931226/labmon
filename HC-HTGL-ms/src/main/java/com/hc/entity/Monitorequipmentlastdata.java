package com.hc.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
public class Monitorequipmentlastdata  implements Serializable {
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
     * 设备最后数据
     */
    private String equipmentlastdata;

    /**
     * 当前温度
     */
    private String currenttemperature;

    /**
     * 当前二氧化碳
     */
    private String currentcarbondioxide;

    /**
     * 当前O2
     */
    private String currento2;

    /**
     * 当前气流
     */
    private String currentairflow;

    /**
     * 当前开门记录
     */
    private String currentdoorstate;

    /**
     * 当前湿度
     */
    private String currenthumidity;

    /**
     * 当前空气质量
     */
    private String currentvoc;

    /**
     * 当前甲醛
     */
    private String currentformaldehyde;

    /**
     * 当前PM2_5
     */
    private String currentpm25;

    /**
     * 当前PM10
     */
    private String currentpm10;

    /**
     * 当前市电是否异常
     */
    private String currentups;

    /**
     * 当前电量
     */
    private String currentqc;

    private static final long serialVersionUID = 1L;


}