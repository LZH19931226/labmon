package com.hc.my.common.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LabSystemEnum {

    HOSPITAL_IS_NOT_BOUND_EQUIPMENT_TYPE("该医院没有绑定设备类型");
    private String message;
}
