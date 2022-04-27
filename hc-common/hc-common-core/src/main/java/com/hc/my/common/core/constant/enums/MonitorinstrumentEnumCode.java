package com.hc.my.common.core.constant.enums;

import lombok.*;

/**
 * 监控设备枚举类
 * @author hc
 */
@Getter
@AllArgsConstructor
public enum MonitorinstrumentEnumCode {
    FAILED_TO_ADD_PROBE("添加探头失败，该医院sn已被占用"),
    FAILED_TO_DELETE("删除失败，当前设备已绑定探头"),
    FAILED_TO_UPDATE_DEVICE("修改设备失败，sn已被占用"),
    FAILED_TO_ADD_DEVICE("添加设备失败,sn已被占用");
    private String message;
}
