package com.hc.my.common.core.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TwoFactorLoginEnum {
    //为1时需要双验证登录
    ONE("1");
    private String code;
}
