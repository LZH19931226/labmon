package com.hc.my.common.core.constant.enums;


/**
 * 适配器的供电状态值
 * @author user
 */
public enum UpsEnum {
    NORMAL_POWER_SUPPLY("适配器供电正常"),
    ABNORMAL_POWER_SUPPLY("适配器供电异常"),
    POWER_ON_TO_POWER_OFF("市电通->市电断； (报警信息：发生断电事件)"),
    POWER_OFF_TO_POWER_OFF("市电断->市电断； (报警信息：持续断电事件， 15 分钟/次)"),
    POWER_OFF_TO_POWER_ON("市电断->市电通； (报警信息：发生通电事件)"),
    MAINS_IS_NORMAL("市电正常"),
    MAINS_ABNORMALITY("市电异常"),
    ZERO("0"),
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5");

    private String code;

    UpsEnum(String code){
     this.code = code;
    }
}
