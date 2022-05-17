package com.hc.my.common.core.redis.namespace;

import lombok.Getter;

@Getter
public enum TcpServiceEnum {
    //用于维护sn和通道关联关系
    TCPCLIENT("TCPCLIENT");

    private String code;

    TcpServiceEnum(String code){
        this.code = code;
    }
}
