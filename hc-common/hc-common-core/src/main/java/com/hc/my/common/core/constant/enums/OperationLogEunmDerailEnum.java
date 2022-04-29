package com.hc.my.common.core.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OperationLogEunmDerailEnum {
    ADD("0", "新增"),
    EDIT("1","修改"),
    REMOVE("2","删除");
    String code;
    String message;
}
