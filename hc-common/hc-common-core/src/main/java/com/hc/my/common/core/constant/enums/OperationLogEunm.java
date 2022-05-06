package com.hc.my.common.core.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author hc
 */
@Getter
@AllArgsConstructor
public enum OperationLogEunm {
    DEVICE_MANAGEMENT("2","设备管理"),
    DEVICE_TYPE_MANAGEMENT("1","设备类型管理"),
    HOSPITALMANAGENT("0", "医院管理");
    String code;
    String message;
}
