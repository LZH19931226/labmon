package com.hc.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LabMonEnumError {
    HOSPITAL_IS_NOT_BOUND_EQUIPMENT_TYPE("该医院没有绑定设备类型"),
    NO_UTILITY_RECORD("市电记录"),
    NO_DATA_FOR_CURRENT_TIME("当前时间未找到数据"),
    THE_DEVICE_HAS_NO_PROBE_INFORMATION("未找到设备探头信息"),
    DEVICE_INFORMATION_NOT_FOUND("未找到设备信息");
    private String message;
}
