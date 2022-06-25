package com.hc.my.common.core.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FactorEnum {
    //开启了双因子登录
    ONE("1");
    private  String code;
}
