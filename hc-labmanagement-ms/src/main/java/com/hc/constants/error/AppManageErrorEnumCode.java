package com.hc.constants.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppManageErrorEnumCode {
    VERSION_NUMBER_ALREADY_EXISTS("版本号已存在");
    private String message;
}
