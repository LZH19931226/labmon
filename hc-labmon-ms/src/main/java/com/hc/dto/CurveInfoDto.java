package com.hc.dto;

import com.hc.application.curvemodel.CurveDataModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 本类用于返回曲线数据格式模型
 */
@Data
@Accessors(chain = true)

public class CurveInfoDto {
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
    @ApiModelProperty("电量")
    private CurveDataModel qc;
    /**
     * mt310dc特殊探头
     */
    @ApiModelProperty("外置探头1温度")
    private CurveDataModel probe1Temp;
    @ApiModelProperty("外置探头1湿度")
    private CurveDataModel probe1rh;
    @ApiModelProperty("外置探头1co2浓度")
    private CurveDataModel probe1Co2;
    @ApiModelProperty("外置探头1o2浓度")
    private CurveDataModel probe1O2;

    @ApiModelProperty("外置探头2温度")
    private CurveDataModel probe2Temp;
    @ApiModelProperty("外置探头2湿度")
    private CurveDataModel probe2rh;
    @ApiModelProperty("外置探头2co2浓度")
    private CurveDataModel probe2Co2;
    @ApiModelProperty("外置探头2o2浓度")
    private CurveDataModel probe2O2;

    @ApiModelProperty("外置探头3温度")
    private CurveDataModel probe3Temp;
    @ApiModelProperty("外置探头3湿度")
    private CurveDataModel probe3rh;
    @ApiModelProperty("外置探头3co2浓度")
    private CurveDataModel probe3Co2;
    @ApiModelProperty("外置探头3o2浓度")
    private CurveDataModel probe3O2;

    @ApiModelProperty("液位")
    private CurveDataModel liquidLevel;

}
