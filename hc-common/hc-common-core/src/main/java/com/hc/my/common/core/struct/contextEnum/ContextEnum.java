package com.hc.my.common.core.struct.contextEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ContextEnum {
    NOT_LOGGED_IN("未登录");
    private String message;
}
