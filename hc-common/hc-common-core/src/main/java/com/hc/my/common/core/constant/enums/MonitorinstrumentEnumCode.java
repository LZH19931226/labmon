package com.hc.my.common.core.constant.enums;

import lombok.*;

/**
 * 监控设备枚举类
 * @author hc
 */
@Getter
@AllArgsConstructor
public enum MonitorinstrumentEnumCode {
    FAILED_TO_ADD_DEVICE("添加设备失败,sn已被占用");
    private String message;
}
