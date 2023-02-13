package com.hc.my.common.core.constant.enums;

import com.hc.my.common.core.exception.IedsException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 *
 */
@Getter
@AllArgsConstructor
public enum FeildEnum {
    USERNAME("用户名"),
    PASSWORD("密码"),
    USER_TYPE("用户类型"),
    IS_THE_USER_AVAILABLE("用户是否可用"),
    CELLPHONE_NUMBER("手机号码"),
    USER_NICKNAME("用户昵称"),
    HOSPITAL_ABBREVIATION("医院简称"),
    FULL_NAME_OF_THE_HOSPITAL("医院全称"),
    USABLE_OR_NOT("是否可用"),
    WHETHER_TO_CALL_THE_POLICE_ALL_DAY("是否全天报警"),
    EQUIPMENT_TYPE("设备类型"),
    WHETHER_TO_SET_TIMEOUT_ALARM("是否设置超时报警"),
    TIMEOUT_ALARM_DURATION("超时报警时长"),
    DEVICE_NAME("设备名称"),
    SN("SN"),
    LOWEST_VALUE("最低值"),
    HIGHEST_VALUE("最高值"),
    CALIBRATE_POSITIVE_AND_NEGATIVE_VALUES("校正正负值"),
    PASSAGEWAY("通道"),
    INTELLIGENT_ALARM_TIMES("智能报警次数"),
    ALARM_SWITCH("报警开关"),
    ALARM_DISABLE_ENABLE("报警禁用启用");
    private String message;

    public static FeildEnum from(String message){
        return Arrays
                .stream(FeildEnum.values())
                .filter(res->message.equals(res.getMessage()))
                .findFirst()
                .orElseThrow(()-> new IedsException("Illegal enum value {}", message));
    }
}
