package com.hc.my.common.core.constant.enums;

import com.hc.my.common.core.exception.IedsException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author hc
 */
@Getter
@AllArgsConstructor
public enum OperationLogEunm {
    USER_MANAGEMENT("4","人员管理"),
    PROBE_MANAGEMENT("3","探头管理"),
    DEVICE_MANAGEMENT("2","设备管理"),
    DEVICE_TYPE_MANAGEMENT("1","设备类型管理"),
    APP_ALARM_SET("5","app报警设置"),
    HOSPITAL_MANAGENT("0", "医院管理");
    String code;
    String message;


    public static OperationLogEunm from(String message){
        return Arrays
                .stream(OperationLogEunm.values())
                .filter(res->message.equals(res.getMessage()))
                .findFirst()
                .orElseThrow(()-> new IedsException("Illegal enum value {}", message));
    }
}
