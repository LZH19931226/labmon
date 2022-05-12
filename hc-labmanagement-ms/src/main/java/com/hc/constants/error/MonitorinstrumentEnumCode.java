package com.hc.constants.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 监控设备枚举类
 * @author hc
 */
@Getter
@AllArgsConstructor
public enum MonitorinstrumentEnumCode {
    THE_LOWER_LIMIT_CANNOT_EXCEED_THE_UPPER_LIMIT("下限值不能超过或等于上限值"),
    PROBE_INFORMATION_ALREADY_EXISTS("添加探头失败，探头信息已存在"),
    FAILED_TO_ADD_PROBE("添加探头失败，该医院sn已被占用"),
    FAILED_TO_DELETE("删除失败，当前设备已绑定探头"),
    FAILED_TO_UPDATE_DEVICE("修改设备失败，sn已被占用"),
    FAILED_TO_ADD_DEVICE("添加设备失败,sn已被占用");
    private String message;
}
