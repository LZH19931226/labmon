package com.hc.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LabMonEnumError {
    NO_DATA_FOR_CURRENT_TIME("当前时间未找到数据"),
    DEVICE_INFORMATION_NOT_FOUND("未找到设备信息");
    private String message;
}
