package com.hc.my.common.core.redis.namespace;

import lombok.Getter;

@Getter
public enum MswkServiceEnum {

    //缓存设备当前值
    L("L");



    private String code;

    MswkServiceEnum(String code){
        this.code = code;
    }
}
