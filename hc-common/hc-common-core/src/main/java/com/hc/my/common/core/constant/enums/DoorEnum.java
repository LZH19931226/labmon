package com.hc.my.common.core.constant.enums;

/**
 * 开关量只上传具体状态值
 * @author user
 */
public enum DoorEnum {
    OPEN_TO_CLOSE("开→关；(报警信息：立即上传)"),
    TURN_OFF_TO_ON("关->开；(报警信息：立即上传)"),
    CONTINUOUS_CLOSING("持续关；(状态信息，3 分钟/次)"),
    CONTINUOUS_ON("持续开；(状态信息，3 分钟/次)"),
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4");
    private String code;

    DoorEnum(String code){
        this.code = code;
    }
}
