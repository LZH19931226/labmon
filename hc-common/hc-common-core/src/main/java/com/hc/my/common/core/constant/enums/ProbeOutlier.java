package com.hc.my.common.core.constant.enums;

public enum ProbeOutlier {

    NO_SENSOR_IS_CONNECTED("未接温度传感器"),
    OUT_OF_TEST_RANGE("若超出测试范围"),
    VALUE_IS_INVALID("值无效"),
    NO_CALIBRATION("未校准"),
    NO_DATA_WAS_OBTAINED("未获取到数据"),
    FFF0("fff0"),
    ZERO("0000"),
    FF00("ff00"),
    FFFF("ffff");
    private String code;

    ProbeOutlier(String code){
        this.code =code;
    };

}
