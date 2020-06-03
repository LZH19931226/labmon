package com.hc.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by 16956 on 2018-08-01.
 */
@Getter
@Setter
@ToString
public class LastDataModel implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 设备编号
     */

    private String equipmentno;

    private String equipmentname;

    /**
     * 记录时间
     */

    private String inputdatetime;



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
     * 当前PM5
     */
    private String currentpm5;
    /**
     * pm05
     */
    private String currentpm05;

    /**
     * 当前市电是否异常
     */
    private String currentups;

    private String currentairflow1;

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


}
