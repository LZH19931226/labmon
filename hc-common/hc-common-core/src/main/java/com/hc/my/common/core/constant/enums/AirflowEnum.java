package com.hc.my.common.core.constant.enums;

import lombok.Getter;

/**
 * 气流的枚举类
 * @author hc
 */
@Getter
public enum AirflowEnum {
    NORMAL_TO_EARLY_WARNING("正常->预警；(报警信息：有漏气倾向)"),
    ALERT_TO_NORMAL("预警->正常；(报警信息：恢复正常)"),
    ALERT_TO_ALARM("预警->报警；(报警信息：发生漏气报警事件)"),
    ALARM_TO_OFF("报警->关闭；(报警信息：手动关闭漏气报警)"),
    NORMAL_OPERATION_OF_EQUIPMENT("设备正常运行；(状态信息，3 分钟/次)"),
    EQUIPMENT_LEAKAGE_WARNING("设备漏气预警；(状态信息，3 分钟/次)"),
    EQUIPMENT_LEAKAGE_ALARM("设备漏气报警；(状态信息，3 分钟/次)"),
    CLOSE_THE_AIR_LEAKAGE_ALARM("手动关闭漏气报警；(状态信息，3 分钟/次)"),
    LOW_AIR_PRESSURE_ALARM_OF_EQUIPMENT("设备气压低报警；(状态信息，3 分钟/次)"),
    MANUALLY_CLOSE_THE_LOW_AIR_PRESSURE_ALARM("手动关闭气压低报警；(状态信息，3 分钟/次)"),
    SLEEP_MODE("休眠模式；(状态信息，3 分钟/次)"),
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9"),
    TEN("10"),
    ELEVEN("11");

    private String code;

    AirflowEnum(String code){
        this.code = code;
    }
}
