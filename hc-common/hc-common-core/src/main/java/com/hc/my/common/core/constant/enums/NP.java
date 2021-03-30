package com.hc.my.common.core.constant.enums;

/**
 * @author LiuZhiHao
 * @date 2019/10/23 14:34
 * 描述:
 **/
import com.hc.my.common.core.exception.IedsException;
import lombok.Getter;

import java.util.Arrays;


@Getter
public enum NP {
    WS("ws", "ws", "wss"),
    MQTT("mqtt", "mqtt", "mqtts"),
    TCP("tcp", "tcp", "tcps"),
    HTTP("http", "http", "https"),
    REDIS("redis", "redis", "rediss"),
    SOLR("solr", "solr", "solrs"),
    ;

    private String name;
    private String schema;
    private String ssl;

    NP(String name, String schema, String ssl) {
        this.name = name;
        this.schema = schema;
        this.ssl = ssl;
    }

    public static NP from(String schema) {
        return Arrays
                .stream(NP.values())
                .filter(s -> Arrays.asList(s.schema, s.ssl).contains(schema))
                .findFirst()
                .orElseThrow(() -> new IedsException("Illegal protocol {} of notify configure.",schema));
    }

}
