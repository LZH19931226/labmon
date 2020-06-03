package com.hc.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by 16956 on 2018-07-31.
 */
@ToString
@Getter
@Setter
public class ParamaterModel {
    @ApiModelProperty("命令id")
    private String cmdid;
    @ApiModelProperty("设备sn号")
    private String SN;
    @ApiModelProperty("CO2")
    private String CO2;
    @ApiModelProperty("O2")
    private String O2;
    @ApiModelProperty("空气质量")
    private String VOC;
    @ApiModelProperty("温度")
    private String TEMP;
    @ApiModelProperty("湿度")
    private String RH;
    @ApiModelProperty("压力")
    private String PRESS;
    @ApiModelProperty("电量")
    private String QC;
    @ApiModelProperty("PM2.5")
    private String PM25;
    @ApiModelProperty("PM10")
    private String PM10;
    @ApiModelProperty("市电")
    private String UPS;
    @ApiModelProperty("开关门记录")
    private String DOOR;

    @ApiModelProperty("甲醛")
    private String OX ;

}
