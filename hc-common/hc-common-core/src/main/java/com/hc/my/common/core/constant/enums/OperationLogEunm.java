package com.hc.my.common.core.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author hc
 */
@Getter
@AllArgsConstructor
public enum OperationLogEunm {
    USER_INFO("4","用户信息"),
    PROBE_MANAGEMENT("3","探头管理"),
    DEVICE_MANAGEMENT("2","设备管理"),
    DEVICE_TYPE_MANAGEMENT("1","设备类型管理"),
    HOSPITALMANAGENT("0", "医院管理");
    String code;
    String message;
}
