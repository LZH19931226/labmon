package com.hc.my.common.core.constant.enums;

import lombok.Getter;

@Getter
public enum ProbeOutlierMt310 {
    //表示未接传感器
    NO_SENSOR_IS_CONNECTED("0"),
    //温度
    THE_TEMPERATURE("1"),
    //湿度
    HUMIDITY("2"),
    //O2 浓度
    OXYGEN_CONCENTRATIONS("3"),
    //CO2 浓度
    CARBON_DIOXIDE_CONCENTRATION("4"),
    //手动模式
    MANUAL_MODE("0"),
    //自动模式
    AUTOMATIC_MODE("1"),

    FFF0("未接传感器"),

    FFF1("超量程"),

    FFF2("值无效");

    private String code;

    ProbeOutlierMt310(String code){
        this.code =code;
    };


}
