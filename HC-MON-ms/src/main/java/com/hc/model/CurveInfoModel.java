package com.hc.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created by 16956 on 2018-08-01.
 */
@ApiModel("返回曲线数据格式模型")
@Getter
@Setter
@ToString
public class CurveInfoModel {

    @ApiModelProperty("PM5")
    private CurveDataModel pm5;
    @ApiModelProperty("PM05")
    private CurveDataModel pm05;
    @ApiModelProperty("PM2.5")
    private CurveDataModel pm25;
    @ApiModelProperty("PM10")
    private CurveDataModel pm10;
    @ApiModelProperty("CO2")
    private CurveDataModel co2;
    @ApiModelProperty("氧气")
    private CurveDataModel o2;
    @ApiModelProperty("湿度")
    private CurveDataModel rh;
    @ApiModelProperty("甲醛")
    private CurveDataModel jq;
    @ApiModelProperty("压力")
    private CurveDataModel press;
    @ApiModelProperty("空气质量")
    private CurveDataModel voc;
    @ApiModelProperty("温度")
    private CurveDataModel temp;
    @ApiModelProperty("COOK培养箱气流")
    private CurveDataModel airflow;
    @ApiModelProperty("左舱室温度")
    private CurveDataModel lefttemp;
    @ApiModelProperty("右舱室温度")
    private CurveDataModel righttemp;
    @ApiModelProperty("一路温度")
    private CurveDataModel temp1;
    @ApiModelProperty("二路温度")
    private CurveDataModel temp2;
    @ApiModelProperty("三路温度")
    private CurveDataModel temp3;
    @ApiModelProperty("四路温度")
    private CurveDataModel temp4;
    @ApiModelProperty("五路温度")
    private CurveDataModel temp5;
    @ApiModelProperty("六路温度")
    private CurveDataModel temp6;
    @ApiModelProperty("七路温度")
    private CurveDataModel temp7;
    @ApiModelProperty("八路温度")
    private CurveDataModel temp8;
    @ApiModelProperty("九路温度")
    private CurveDataModel temp9;
    @ApiModelProperty("十路温度")
    private CurveDataModel temp10;
    @ApiModelProperty("差值")
    private CurveDataModel difftemp;
    @ApiModelProperty("左盖板温度")
    private CurveDataModel leftcovertemp;
    @ApiModelProperty("左底板温度")
    private CurveDataModel leftendtemp;
    @ApiModelProperty("左气流")
    private CurveDataModel leftair;
    @ApiModelProperty("右盖板温度")
    private CurveDataModel rightcovertemp;
    @ApiModelProperty("右底板温度")
    private CurveDataModel rightendtemp;
    @ApiModelProperty("右气流")
    private CurveDataModel rightair;
    @ApiModelProperty("N2")
    private CurveDataModel n2;
    @ApiModelProperty("左湿度")
    private CurveDataModel leftCompartmentHumidity;
    @ApiModelProperty("右湿度")
    private CurveDataModel rightCompartmentHumidity;
    @ApiModelProperty("探头上下限")
    private List<InstrumentMonitorInfoModel> instrumentMonitorInfoModel;
}
