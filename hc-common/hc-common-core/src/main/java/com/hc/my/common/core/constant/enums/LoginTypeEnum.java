package com.hc.my.common.core.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoginTypeEnum {
    //网页登录
    H5("h5"),
    //app登录
    ANDROID("android");
    private String code;
}
