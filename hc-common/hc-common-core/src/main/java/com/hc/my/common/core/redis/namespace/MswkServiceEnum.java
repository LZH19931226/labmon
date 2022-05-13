package com.hc.my.common.core.redis.namespace;

import lombok.Getter;

@Getter
public enum MswkServiceEnum {

    //
    xxx("xxx");



    private String code;

    MswkServiceEnum(String code){
        this.code = code;
    }
}
