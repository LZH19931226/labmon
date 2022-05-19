package com.hc.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LabMonEnumError {
    DEVICE_INFORMATION_NOT_FOUND("未找到设备信息");
    private String message;
}
