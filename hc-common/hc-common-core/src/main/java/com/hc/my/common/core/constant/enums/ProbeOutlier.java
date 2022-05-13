package com.hc.my.common.core.constant.enums;

import lombok.Getter;

/**
 * 设备异常值
 * @author user
 */
@Getter
public enum ProbeOutlier {

    NO_SENSOR_IS_CONNECTED("未接温度传感器"),
    OUT_OF_TEST_RANGE("超出量程"),
    VALUE_IS_INVALID("值无效"),
    THE_RANGE_FILTER_VALUE_IS_INVALID("量程过滤值无效"),
    NO_CALIBRATION("未校准"),
    NO_DATA_WAS_OBTAINED("未获取到数据"),
    TEMPERATURE_CONTROL_OFF("表示温度控制关闭，显示“OFF”"),
    COMPARTMENT_DOOR_OPEN("表示仓室门开启，显示“LID”"),
    ABNORMAL_TEMPERATURE_CONTROL("表示温度控制异常，显示“Err”"),
    MAIN_SWITCH_OFF("表示总开关关闭，但未断电，无显示"),
    AIRFLOW_CONTROL_OFF("表示流量控制关闭，显示“OFF””"),
    GAS_FLOW_IS_UNSTABLE("表示气体流量不稳定，显示“Pu"),
    LOW_INLET_PRESSURE("表示进气口压力低，显示“CO2”"),
    AIRFLOW_OUT_OF_RANGE("表示无气或气流超出范围，显示“Err”"),
    ZERO("0000"),
    F000("f000"),
    FF00("ff00"),
    FFF0("fff0"),
    FFFF("ffff"),
    FFF1("fff1"),
    FFF2("fff2"),
    FFF3("fff3"),
    OXFFF0("fff0"),
    OXFFF1("fff1"),
    OXFFF2("fff2"),
    OXFFF3("fff3"),
    OXFFF4("fff4"),
    OXFFF5("fff5"),
    FFFFFFF0("fffffff0"),
    FFFFFFF1("fffffff1"),
    F1("f1"),
    F0("f0");

    private String code;

    ProbeOutlier(String code){
        this.code =code;
    };


}
