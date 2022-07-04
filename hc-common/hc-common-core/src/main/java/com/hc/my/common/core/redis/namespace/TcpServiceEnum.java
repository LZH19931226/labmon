package com.hc.my.common.core.redis.namespace;

import lombok.Getter;

@Getter
public enum TcpServiceEnum {
    //用于维护sn和通道关联关系
    TCPCLIENT("TCPCLIENT"),
    //用于维护通道和sn关联关系
    CHANNELCLIENT("CHANNELCLIENT");

    private String code;

    TcpServiceEnum(String code){
        this.code = code;
    }
}
