package com.hc.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * Created by 16956 on 2018-08-30.
 */
@ApiModel("返回当前值模型")
@Getter
@Setter
@ToString
public class CurrentInfoModel {
    @ApiModelProperty("PM2.5")
    private Object PM25;
    @ApiModelProperty("PM10")
    private Object PM10;
    @ApiModelProperty("CO2")
    private Object CO2;
    @ApiModelProperty("氧气")
    private Object O2;
    @ApiModelProperty("湿度")
    private Object RH;
    @ApiModelProperty("甲醛")
    private Object JQ;
    @ApiModelProperty("压力")
    private Object PRESS;
    @ApiModelProperty("空气质量")
    private Object VOC;
    @ApiModelProperty("温度")
    private Object TEMP;
    @ApiModelProperty("门")
    private Object DOOR;
    @ApiModelProperty("电量")
    private Object QC;

}
