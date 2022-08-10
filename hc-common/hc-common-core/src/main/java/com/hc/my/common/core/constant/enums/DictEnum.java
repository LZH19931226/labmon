package com.hc.my.common.core.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DictEnum {
    //所有的开关开启为1，其他的为未开启
    TURN_ON("1","开关开启"),
    OFF("0","不是全天报警"),
    //设备类型
    ENVIRONMENT("1","环境"),
    INCUBATOR("2","培养箱"),
    LIQUID_NITROGEN_TANK("3","液氮罐"),
    REFRIGERATOR("4","冰箱"),
    CONSOLE("5","操作台"),
    MAINS("6","市电"),
    //默认联系方式
    PHONE_SMS("0","电话+短信"),
    PHONE("1","电话"),
    SMS("2","短信"),
    UNOPENED_CONTACT_DETAILS("3","未开启联系方式");
    private String code;
    private String message;
}
