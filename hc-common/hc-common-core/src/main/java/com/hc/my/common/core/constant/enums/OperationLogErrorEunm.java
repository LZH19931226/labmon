package com.hc.my.common.core.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OperationLogErrorEunm {
    START_TIME_OR_END_TIME_CANNOT_BE_EMPTY("开始时间或结束时间不能为空"),
    END_TIME_CANNOT_BE_EARLIER_THAN_START_TIME("结束时间不能早于开始时间");
    private String code;
}
