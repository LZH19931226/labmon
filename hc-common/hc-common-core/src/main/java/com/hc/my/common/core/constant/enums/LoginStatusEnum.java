package com.hc.my.common.core.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LoginStatusEnum {
    //第一次登录
    ZERO("0"),
    //非第一次登录
    ONE("1");
    private String code;
}
