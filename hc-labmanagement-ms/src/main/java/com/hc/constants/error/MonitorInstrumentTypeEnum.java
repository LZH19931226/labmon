package com.hc.constants.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MonitorInstrumentTypeEnum {
    NAME_ALREADY_EXISTS("检测类型名称已存在"),
    BINDING_INFO_EXISTS("该设备下有绑定检测内容，请先删除");
    private String message;
}
