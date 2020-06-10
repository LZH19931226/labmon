package com.hc.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "monitorequipmentlastdata")
@Entity
@Getter
@Setter
@ToString
public class Monitorequipmentlastdata implements Serializable {

    @Id
    private String pkid;
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
     * 培养箱气流
     */
    private String currentairflow1;

    /**
     * 当前PM10
     */
    private String currentpm10;
    /**
     * 当前PM0.5
     */
    private String currentpm05;
    /**
     * 当前PM5
     */
    private String currentpm5;

    /**
     * 当前市电是否异常
     */
    private String currentups;

    /**
     * 当前电量
     */
    private String currentqc;

    private String currenttemperature1;
    private String currenttemperature2;

    private String currenttemperature3;

    private String currenttemperature4;

    private String currenttemperature5;

    private String currenttemperature6;

    private String currenttemperature7;

    private String currenttemperature8;

    private String currenttemperature9;

    private String currenttemperature10;

    private String currentlefttemperature;

    private String currentrigthtemperature;

    private String currenttemperaturediff;
    @ApiModelProperty("左盖板温度")
    private String currentleftcovertemperature;
    @ApiModelProperty("左底板温度")
    private String currentleftendtemperature;
    @ApiModelProperty("左气流")
    private String currentleftairflow;
    @ApiModelProperty("右盖板温度")
    private String currentrightcovertemperature;
    @ApiModelProperty("右底板温度")
    private String currentrightendtemperature;
    @ApiModelProperty("右气流")
    private String currentrightairflow;

    private String currentn2;

    private static final long serialVersionUID = 1L;


}