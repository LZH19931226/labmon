package com.hc.my.common.core.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author hc
 */
@Getter
@AllArgsConstructor
public enum OperationLogEunm {

    HOSPITALMANAGENT("0", "医院管理");
    String code;
    String message;
}
