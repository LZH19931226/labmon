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
    APP_EDIT_PROBE("5","app修改探头"),
    APP_EDIT_EQ("6","app修改设备"),
    APP_EDIT_EQ_TYPE("7","app修改设备类型"),
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
    public static OperationLogEunm fromCode(String code){
        return Arrays
                .stream(OperationLogEunm.values())
                .filter(res->code.equals(res.getCode()))
                .findFirst()
                .orElseThrow(()-> new IedsException("Illegal enum value {}", code));
    }
}
