package com.hc.my.common.core.constant.enums;

/**
 * 设备异常值
 * @author user
 */
public enum ProbeOutlier {

    NO_SENSOR_IS_CONNECTED("未接温度传感器"),
    OUT_OF_TEST_RANGE("若超出测试范围"),
    VALUE_IS_INVALID("值无效"),
    NO_CALIBRATION("未校准"),
    NO_DATA_WAS_OBTAINED("未获取到数据"),
    TEMPERATURE_CONTROL_OFF("表示温度控制关闭，显示“OFF”"),
    COMPARTMENT_DOOR_OPEN("表示仓室门开启，显示“LID”"),
    ABNORMAL_TEMPERATURE_CONTROL("表示温度控制异常，显示“Err”"),
    MAIN_SWITCH_OFF("表示总开关关闭，但未断电，无显示"),
    ZERO("0000"),
    F000("f000"),
    FF00("ff00"),
    FFF0("fff0"),
    FFFF("ffff"),
    FFF1("fff1"),
    FFF2("fff2"),
    FFF3("fff3"),
    OXFFF0("0xfff0"),
    OXFFF1("0xfff1"),
    OXFFF2("0xfff2"),
    OXFFF3("0xfff3"),
    OXFFF4("0xfff4"),
    F1("f1"),
    F0("f0");

    private String code;

    ProbeOutlier(String code){
        this.code =code;
    };


}
